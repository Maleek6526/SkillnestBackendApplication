package com.skillnest.jobservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkImageRequest {
    private String jobSeekerId;
    private String caption;
}
