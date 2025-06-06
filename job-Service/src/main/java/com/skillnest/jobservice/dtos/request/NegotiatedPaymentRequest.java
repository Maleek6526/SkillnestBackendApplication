package com.skillnest.jobservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NegotiatedPaymentRequest {
    private String jobId;
    private BigDecimal negotiatedPaymentAmount;
}
