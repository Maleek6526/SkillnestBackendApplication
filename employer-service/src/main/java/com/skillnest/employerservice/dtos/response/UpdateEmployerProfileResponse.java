package com.skillnest.employerservice.dtos.response;

import com.skillnest.employerservice.data.model.Employer;
import lombok.Data;

@Data
public class UpdateEmployerProfileResponse {
    private String message;
    private Employer employer;
}
