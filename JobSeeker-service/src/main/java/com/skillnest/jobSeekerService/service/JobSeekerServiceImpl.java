package com.skillnest.jobSeekerService.service;

import com.cloudinary.Cloudinary;
import com.skillnest.jobSeekerService.data.model.*;
import com.skillnest.jobSeekerService.data.repository.*;
import com.skillnest.jobSeekerService.dtos.JobDTO;
import com.skillnest.jobSeekerService.dtos.UserDto;
import com.skillnest.jobSeekerService.dtos.request.*;
import com.skillnest.jobSeekerService.dtos.response.*;
import com.skillnest.jobSeekerService.exception.JobSeekerNotFoundException;
import com.skillnest.jobSeekerService.exception.NoAvailabilitySlotException;
import com.skillnest.jobSeekerService.exception.NoDocumentFoundException;
import com.skillnest.jobSeekerService.exception.NoImageFoundException;
import com.skillnest.jobSeekerService.feign.JobInterface;
import com.skillnest.jobSeekerService.feign.JobSeekerInterface;
import com.skillnest.jobSeekerService.mapper.JobSeekerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JobSeekerServiceImpl implements JobSeekerService{

    private final JobSeekerInterface jobSeekerInterface;
    private final JobInterface jobInterface;
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

        try {
            String uploadedProfileUrl = cloudinary
                    .uploader()
                    .upload(registerJobSeekerRequest.getProfilePictureUrl().getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString(), "resource_type", "auto"))
                    .get("secure_url")
                    .toString();
            String uploadDocuments = cloudinary
                    .uploader()
                    .upload(registerJobSeekerRequest.getDocumentUrl().getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString(), "resource_type", "auto"))
                    .get("secure_url")
                    .toString();
            String uploadResume = cloudinary
                    .uploader()
                    .upload(registerJobSeekerRequest.getResumeUrl().getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString(), "resource_type", "auto"))
                    .get("secure_url")
                    .toString();

            jobSeeker.setResumeUrl(uploadResume);
            jobSeeker.setDocumentUrl(uploadDocuments);
            jobSeeker.setProfilePictureUrl(uploadedProfileUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
        jobSeekerRepository.save(jobSeeker);

        return JobSeekerMapper.mapToRegisterJobSeekerResponse("Job seeker registered successfully", jobSeeker);
    }
    @Override
    public TakeJobResponse takeJob(TakeJobRequest request) {
        ResponseEntity<JobDTO> response = jobInterface.getJobById(request.getJobId());
        JobDTO job = response.getBody();
        if(job == null){
            throw new JobSeekerNotFoundException("Job  not found");
        }
        TakeJobRequest takeJobRequest = JobSeekerMapper.mapToTakeJobRequest(job, request);

        Optional<JobSeeker> existingJobSeeker = jobSeekerRepository.findById(request.getJobSeekerId());
        if (existingJobSeeker.isEmpty()) {
            throw new JobSeekerNotFoundException("Job seeker not found");
        }
        JobSeeker jobSeeker = existingJobSeeker.get();
         return JobSeekerMapper.mapToTakeJobResponse("Job successfully taken by JobSeeker", jobSeeker.getId());
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
                throw new NoAvailabilitySlotException("No image found");
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
        Optional<VerificationDocument> documents = verificationDocumentRepository.findByJobSeekerId(jobSeekerId);
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
        Optional<AvailabilitySlot> existingSlot = availabilitySlotRepository.findById(jobSeeker.getId());
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

            jobSeeker.setDocumentUrl(verificationDocument.getDocumentUrl());
            jobSeekerRepository.save(jobSeeker);

            return JobSeekerMapper.mapToUploadDocumentResponse("Uploaded successfully", verificationDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
