package com.skillnest.jobservice.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostJobRequest {
    private String title;
    private String description;
    private String employerId;
    private String location;
    private MultipartFile jobImages;
    private List<String> requiredSkills;
    private BigDecimal proposedPayment;
    private String jobType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline;
    private String contactInfo;
    private String jobId;
}
