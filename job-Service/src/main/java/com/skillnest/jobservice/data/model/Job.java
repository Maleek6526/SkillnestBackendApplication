package com.skillnest.jobservice.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skillnest.jobservice.data.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("job")
@Builder
public class Job {
    @Id
    private String id;
    private String title;
    private String description;
    private String employerId;
    private String jobSeekerId;
    private String location;
    private WorkImage workImage;
    private List<String> requiredSkills;
    private BigDecimal proposedPayment;
    private BigDecimal negotiatedPayment;
    private String jobType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline;
    private String contactInfo;
    private JobStatus status;
    @CreatedDate
    private LocalDateTime postedDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdatedDate;
    private boolean jobSeekerVerifiedCompletion;
    private boolean employerVerifiedCompletion;
}
