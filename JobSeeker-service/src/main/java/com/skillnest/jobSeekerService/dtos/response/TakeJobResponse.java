package com.skillnest.jobSeekerService.dtos.response;

import lombok.Data;

@Data
public class TakeJobResponse {
    private String message;
    private String jobId;
    private String jobSeekerId;
}
