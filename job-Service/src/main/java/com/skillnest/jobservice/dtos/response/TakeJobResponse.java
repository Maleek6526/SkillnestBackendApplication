package com.skillnest.jobservice.dtos.response;

import lombok.Data;

@Data
public class TakeJobResponse {
    private String jobId;
    private String message;
    private String jobSeekerId;
}
