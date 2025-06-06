package com.skillnest.jobservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyCompleteJobRequest {
    private String jobId;
    private String userId;
    private boolean isJobSeeker;
}
