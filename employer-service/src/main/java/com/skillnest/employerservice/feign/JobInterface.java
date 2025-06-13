package com.skillnest.employerservice.feign;

import com.skillnest.employerservice.dtos.JobDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOB-SERVICE", url = "http://localhost:8070")
public interface JobInterface {
    @GetMapping("/job-service/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable String id);
}
