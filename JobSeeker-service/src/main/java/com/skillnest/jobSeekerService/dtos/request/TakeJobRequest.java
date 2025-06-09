package com.skillnest.jobSeekerService.dtos.request;

import lombok.Data;

@Data
public class TakeJobRequest {
    private String jobSeekerId;
    private String jobId;
}
