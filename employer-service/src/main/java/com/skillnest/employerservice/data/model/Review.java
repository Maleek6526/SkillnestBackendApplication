package com.skillnest.employerservice.data.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document("review")
public class Review {
    private String reviewId;
    private String jobId;
    private int employerId;
    private int seekerId;
    private String comment;
    private double rating;
    private LocalDateTime reviewDate;
}
