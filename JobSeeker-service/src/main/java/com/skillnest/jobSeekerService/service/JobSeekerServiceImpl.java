package com.skillnest.jobSeekerService.service;

import com.cloudinary.Cloudinary;
import com.skillnest.jobSeekerService.data.model.*;
import com.skillnest.jobSeekerService.data.repository.*;
import com.skillnest.jobSeekerService.dtos.UserDto;
import com.skillnest.jobSeekerService.dtos.request.*;
import com.skillnest.jobSeekerService.dtos.response.*;
import com.skillnest.jobSeekerService.exception.JobSeekerNotFoundException;
import com.skillnest.jobSeekerService.exception.NoDocumentFoundException;
import com.skillnest.jobSeekerService.exception.NoImageFoundException;
import com.skillnest.jobSeekerService.feign.JobSeekerInterface;
import com.skillnest.jobSeekerService.mapper.JobSeekerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService{

    private JobSeekerInterface jobSeekerInterface;

    private static final String JOB_SERVICE_BASE_URL = "http://localhost:8070/job-service";
    private final RestTemplate restTemplate;
    private final JobSeekerRepository jobSeekerRepository;
    private final Cloudinary cloudinary;
    private final VerificationDocumentRepository verificationDocumentRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    @Override
    public RegisterJobSeekerResponse completeProfile(RegisterJobSeekerRequest registerJobSeekerRequest){
        ResponseEntity<UserDto> response = jobSeekerInterface.findUserById(registerJobSeekerRequest.getUserId());
        UserDto user = response.getBody();
        if(user == null){
            throw new JobSeekerNotFoundException("Job seeker not found");
        }
        JobSeeker jobSeeker = JobSeekerMapper.mapToRegisterJobSeeker(user, registerJobSeekerRequest);
        jobSeekerRepository.save(jobSeeker);

        return JobSeekerMapper.mapToRegisterJobSeekerResponse("Job seeker registered successfully", jobSeeker);
    }

    @Override
    public UpdateJobSeekerProfileResponse updateProfile(UpdateJobSeekerProfileRequest request) {
        Optional<JobSeeker> existingJobSeeker = jobSeekerRepository.findByUserId(request.getUserId());
        if (existingJobSeeker.isEmpty()){
            throw new JobSeekerNotFoundException("Job seeker not found");
        }
        JobSeeker jobSeeker = existingJobSeeker.get();
        JobSeekerMapper.mapToUpdateJobSeekerProfile(jobSeeker, request);
        jobSeekerRepository.save(jobSeeker);
        return JobSeekerMapper.mapToUpdateJobSeekerProfileResponse("Job seeker updated Successfully", jobSeeker);
    }

    @Override
    public UpdateJobSeekerProfileResponse getProfile(String userid) {
        Optional<JobSeeker> existingJobSeeker = jobSeekerRepository.findByUserId(userid);
        if (existingJobSeeker.isEmpty()){
            throw new JobSeekerNotFoundException("Job seeker not found");
        }
        JobSeeker jobSeeker = existingJobSeeker.get();
        return JobSeekerMapper.mapToUpdateJobSeekerProfileResponse("Job seeker profile found", jobSeeker);
    }

    @Override
    public AvailabilitySlotResponse setAvailability(List<AvailabilitySlotRequest> slots) {
        Optional<JobSeeker> existingJobSeeker = jobSeekerRepository.findById(slots.getFirst().getJobSeekerId());
        if(existingJobSeeker.isEmpty()){
            throw new JobSeekerNotFoundException("Job seeker not found");
        }
        JobSeeker jobSeeker = existingJobSeeker.get();
        jobSeeker.setAvailabilitySlotIds(JobSeekerMapper.mapToAvailabilitySlot(jobSeeker.getId(),slots));
        jobSeekerRepository.save(jobSeeker);

        Optional<AvailabilitySlot> existingSlot = availabilitySlotRepository.findById(jobSeeker.getAvailabilitySlotIds());
        if(existingSlot.isEmpty()){
            throw new NoImageFoundException("No image found");
        }
        return JobSeekerMapper.mapToAvailabilitySlotResponse("Availability has been set successfully", existingSlot.get());
    }

    @Override
    public VerificationDocumentResponse getDocuments(String jobSeekerId) {
        Optional<JobSeeker> existingJobSeeker = jobSeekerRepository.findById(jobSeekerId);
        if(existingJobSeeker.isEmpty()){
            throw new JobSeekerNotFoundException("Job seeker not found");
        }
        JobSeeker jobSeeker = existingJobSeeker.get();
        Optional<VerificationDocument> documents = verificationDocumentRepository.findById(jobSeeker.getDocumentIds());
        if (documents.isEmpty()) {
            throw new NoDocumentFoundException("No documents found for this job seeker");
        }

        return JobSeekerMapper.mapToVerificationDocumentResponse("Document found", documents.get());
    }

    @Override
    public List<AvailabilitySlotResponse> getAvailability(String jobSeekerId) {
        Optional<JobSeeker> existingJobSeeker = jobSeekerRepository.findById(jobSeekerId);
        if(existingJobSeeker.isEmpty()){
            throw new JobSeekerNotFoundException("Job seeker not found");
        }
        JobSeeker jobSeeker = existingJobSeeker.get();
        List<AvailabilitySlot> existingSlot = availabilitySlotRepository.findAllById(jobSeeker.getWorkImageIds());
        if(existingSlot.isEmpty()){
            throw new NoImageFoundException("No image found");
        }
        return existingSlot.stream()
                .map(slot -> JobSeekerMapper.mapToAvailabilitySlotResponse("Found image", slot))
                .collect(Collectors.toList());
    }



    @Override
    public UploadDocumentsResponse uploadDocuments(VerificationDocumentRequest documents) {
        Optional<JobSeeker> jobSeekerOpt = jobSeekerRepository.findById(documents.getJobSeekerId());
        if (jobSeekerOpt.isEmpty()) {
            throw new JobSeekerNotFoundException("Job seeker not found");
        }

        JobSeeker jobSeeker = jobSeekerOpt.get();

        try {
            String uploadedUrl = cloudinary
                    .uploader()
                    .upload(documents.getDocumentFile().getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString(), "resource_type", "auto"))
                    .get("secure_url")
                    .toString();

            VerificationDocument verificationDocument = JobSeekerMapper.mapToVerificationDocument(jobSeeker.getId(), documents);
            verificationDocument.setDocumentUrl(uploadedUrl);
            verificationDocumentRepository.save(verificationDocument);

            jobSeeker.setDocumentIds(verificationDocument.getId());
            jobSeekerRepository.save(jobSeeker);

            return JobSeekerMapper.mapToUploadDocumentResponse("Uploaded successfully", verificationDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TakeJobResponse takeJob(TakeJobRequest request) {
        Optional<JobSeeker> existingJobSeeker = jobSeekerRepository.findById(request.getJobSeekerId());
        if (existingJobSeeker.isEmpty()) {
            throw new JobSeekerNotFoundException("Job seeker not found");
        }
        
        String jobServiceUrl = JOB_SERVICE_BASE_URL + "/take";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(jobServiceUrl, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                TakeJobResponse takeJobResponse = new TakeJobResponse();
                takeJobResponse.setMessage("Job taken successfully by job seeker: " + request.getJobSeekerId());
                takeJobResponse.setJobId(request.getJobId());
                takeJobResponse.setJobSeekerId(request.getJobSeekerId());
                return takeJobResponse;
            } else {
                throw new RuntimeException("Failed to take job. Job Service responded with status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Job not found in Job Service or endpoint invalid.");
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Invalid request to Job Service: " + e.getResponseBodyAsString());
            } else {
                throw new RuntimeException("Error communicating with Job Service: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while trying to take the job: " + e.getMessage(), e);
        }
    }

}
