package com.skillnest.employerservice.service;

import com.skillnest.employerservice.dtos.request.PostJobRequest;
import com.skillnest.employerservice.dtos.request.RegisterEmployerRequest;
import com.skillnest.employerservice.dtos.request.UpdateEmployerProfileRequest;
import com.skillnest.employerservice.dtos.response.PostJobResponse;
import com.skillnest.employerservice.dtos.response.RegisterEmployerResponse;
import com.skillnest.employerservice.dtos.response.UpdateEmployerProfileResponse;
import org.springframework.stereotype.Service;

@Service
public interface EmployerService {
    RegisterEmployerResponse completeProfile(RegisterEmployerRequest request);

    PostJobResponse postJob (PostJobRequest request);

    UpdateEmployerProfileResponse updateProfile(UpdateEmployerProfileRequest request);

    UpdateEmployerProfileResponse getProfile(String userid);
}
