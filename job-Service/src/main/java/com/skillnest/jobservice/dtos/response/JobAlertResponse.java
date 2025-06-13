package com.skillnest.jobservice.dtos.response;

import com.skillnest.jobservice.data.enums.AlertFrequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobAlertResponse {
    private String jobId;
    private String jobSeekerId;
    private String alertName;
    private List<String> keywords;
    private String location;
    private BigDecimal minProposedPayment;
    private String jobType;
    private AlertFrequency notificationFrequency;
    private LocalDateTime createdAt;
    private LocalDateTime lastNotifiedAt;
    private boolean active;
}
