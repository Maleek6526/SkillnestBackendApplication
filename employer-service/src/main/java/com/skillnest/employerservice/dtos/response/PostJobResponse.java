package com.skillnest.employerservice.dtos.response;

import lombok.Data;

@Data
public class PostJobResponse {
    private String message;
    private String jobId;
    private String employerId;
}
