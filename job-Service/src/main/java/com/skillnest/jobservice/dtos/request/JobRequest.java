package com.skillnest.jobservice.dtos.request;

import com.skillnest.jobservice.data.enums.JobStatus;
import com.skillnest.jobservice.data.model.WorkImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobRequest {
    private String title;
    private String description;
    private WorkImage workImage;
    private String employerId;
    private String location;
    private MultipartFile jobImages;
    private List<String> requiredSkillIds;
    private BigDecimal proposedPayment;
    private String jobType;
    private LocalDateTime deadline;
    private String contactInfo;
}
