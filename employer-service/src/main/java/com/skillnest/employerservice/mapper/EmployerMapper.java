package com.skillnest.employerservice.mapper;

import com.skillnest.employerservice.data.model.Employer;
import com.skillnest.employerservice.dtos.JobDTO;
import com.skillnest.employerservice.dtos.UserDto;
import com.skillnest.employerservice.dtos.request.PostJobRequest;
import com.skillnest.employerservice.dtos.request.RegisterEmployerRequest;
import com.skillnest.employerservice.dtos.request.UpdateEmployerProfileRequest;
import com.skillnest.employerservice.dtos.response.PostJobResponse;
import com.skillnest.employerservice.dtos.response.RegisterEmployerResponse;
import com.skillnest.employerservice.dtos.response.UpdateEmployerProfileResponse;

import java.util.UUID;

public class EmployerMapper {
    public static Employer mapToRegisterEmployer(UserDto user, RegisterEmployerRequest request) {
        Employer employer = new Employer();
        employer.setId(user.getId());
        employer.setLocation(request.getLocation());
        employer.setBio(request.getBio());
        employer.setFullName(request.getFullName());
        employer.setPhoneNumber(request.getPhoneNumber());
        employer.setCompanyName(request.getCompanyName());
        employer.setCompanyDescription(request.getCompanyDescription());
        return employer;
    }

    public static RegisterEmployerResponse mapToRegisterEmployerResponse(String profileCompleted, Employer employer) {
        RegisterEmployerResponse response = new RegisterEmployerResponse();
        response.setEmployer(employer);
        response.setMessage(profileCompleted);
        return response;
    }

    public static PostJobRequest mapToPostJobRequest(JobDTO job, PostJobRequest request) {
        PostJobRequest postJobRequest = new PostJobRequest();
        postJobRequest.setEmployerId(request.getEmployerId());
        postJobRequest.setJobId(job.getJobId());
        return postJobRequest;
    }

    public static PostJobResponse mapToPostJobResponse(String jobPosted, String id) {
        PostJobResponse postJobResponse = new PostJobResponse();
        postJobResponse.setEmployerId(id);
        postJobResponse.setMessage(jobPosted);
        return postJobResponse;
    }

    public static void mapToUpdateEmployerProfile(Employer employer, UpdateEmployerProfileRequest request) {
        employer.setFullName(request.getFullName());
        employer.setLocation(request.getLocation());
        employer.setPhoneNumber(request.getPhoneNumber());
        employer.setProfilePictureUrl(request.getProfilePictureUrl());
    }

    public static UpdateEmployerProfileResponse mapToUpdateEmployerProfileResponse(String jobSeekerUpdatedSuccessfully, Employer employer) {
        UpdateEmployerProfileResponse response = new UpdateEmployerProfileResponse();
        response.setEmployer(employer);
        response.setMessage(jobSeekerUpdatedSuccessfully);
        return response;
    }
}
