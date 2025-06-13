package com.skillnest.employerservice.dtos.response;

import com.skillnest.employerservice.data.model.Employer;
import lombok.Data;

@Data
public class RegisterEmployerResponse {
    private String message;
    private Employer employer;
}
