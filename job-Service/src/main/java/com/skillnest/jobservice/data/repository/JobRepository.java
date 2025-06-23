package com.skillnest.jobservice.data.repository;

import com.skillnest.jobservice.data.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByProposedPaymentBetween(BigDecimal min, BigDecimal max);
    List<Job> findByProposedPaymentGreaterThanEqual(BigDecimal min);
    List<Job> findByProposedPaymentLessThanEqual(BigDecimal max);
    List<Job> findAllByJobType(String jobType);
    List<Job> findAllByLocation(String location);
}

