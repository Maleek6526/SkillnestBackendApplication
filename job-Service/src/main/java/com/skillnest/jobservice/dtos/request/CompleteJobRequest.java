package com.skillnest.jobservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompleteJobRequest {
    private String jobId;
    private String userId;
    private boolean isJobSeeker;
}
