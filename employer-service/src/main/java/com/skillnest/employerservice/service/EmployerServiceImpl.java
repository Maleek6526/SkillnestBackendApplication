package com.skillnest.employerservice.service;

import com.cloudinary.Cloudinary;
import com.skillnest.employerservice.data.model.Employer;
import com.skillnest.employerservice.data.repository.EmployerRepository;
import com.skillnest.employerservice.dtos.JobDTO;
import com.skillnest.employerservice.dtos.UserDto;
import com.skillnest.employerservice.dtos.request.PostJobRequest;
import com.skillnest.employerservice.dtos.request.RegisterEmployerRequest;
import com.skillnest.employerservice.dtos.request.UpdateEmployerProfileRequest;
import com.skillnest.employerservice.dtos.response.PostJobResponse;
import com.skillnest.employerservice.dtos.response.RegisterEmployerResponse;
import com.skillnest.employerservice.dtos.response.UpdateEmployerProfileResponse;
import com.skillnest.employerservice.exception.EmployerNotFoundException;
import com.skillnest.employerservice.feign.JobInterface;
import com.skillnest.employerservice.feign.EmployerInterface;
import com.skillnest.employerservice.mapper.EmployerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final EmployerInterface employerInterface;
    private final JobInterface jobInterface;
    private final Cloudinary cloudinary;

    @Override
    public RegisterEmployerResponse completeProfile(RegisterEmployerRequest request){
        ResponseEntity<UserDto> response = employerInterface.findUserById(request.getUserId());
        UserDto user = response.getBody();
        if(user == null){
            throw new EmployerNotFoundException("Employer not found");
        }
        Employer employer = EmployerMapper.mapToRegisterEmployer(user, request);
        try {
            String uploadedProfileUrl = cloudinary
                    .uploader()
                    .upload(request.getProfilePictureUrl().getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString(), "resource_type", "auto"))
                    .get("secure_url")
                    .toString();
            String uploadDocuments = cloudinary
                    .uploader()
                    .upload(request.getDocumentUrl().getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString(), "resource_type", "auto"))
                    .get("secure_url")
                    .toString();

            employer.setDocumentUrl(uploadDocuments);
            employer.setProfilePictureUrl(uploadedProfileUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
        employerRepository.save(employer);
        return EmployerMapper.mapToRegisterEmployerResponse("Profile completed", employer);
    }
    @Override
    public PostJobResponse postJob(PostJobRequest request){
        ResponseEntity<PostJobResponse> response = jobInterface.postJob(request);
        PostJobResponse job = response.getBody();
        if(job == null){
            throw new EmployerNotFoundException("Job  not found");
        }
        PostJobRequest postJobRequest = EmployerMapper.mapToPostJobRequest(job, request);
        Optional<Employer> existingEmployer = employerRepository.findById(request.getEmployerId());
        if (existingEmployer.isEmpty()) {
            throw new EmployerNotFoundException("Employer not found");
        }
        Employer employer = existingEmployer.get();
        return EmployerMapper.mapToPostJobResponse("Job posted", employer.getId());
    }
    @Override
    public UpdateEmployerProfileResponse updateProfile(UpdateEmployerProfileRequest request) {
        Optional<Employer> existingEmployer = employerRepository.findById(request.getUserId());
        if (existingEmployer.isEmpty()){
            throw new EmployerNotFoundException("Employer not found");
        }
        Employer employer = existingEmployer.get();
        EmployerMapper.mapToUpdateEmployerProfile(employer, request);
        employerRepository.save(employer);
        return EmployerMapper.mapToUpdateEmployerProfileResponse("Job seeker updated Successfully", employer);
    }

    @Override
    public UpdateEmployerProfileResponse getProfile(String userid) {
        Optional<Employer> existingEmployer = employerRepository.findById(userid);
        if (existingEmployer.isEmpty()){
            throw new EmployerNotFoundException("Job seeker not found");
        }
        Employer employer = existingEmployer.get();
        return EmployerMapper.mapToUpdateEmployerProfileResponse("Job seeker profile found", employer);
    }
//    @Override
//    public ReviewResponse sendReview


}
