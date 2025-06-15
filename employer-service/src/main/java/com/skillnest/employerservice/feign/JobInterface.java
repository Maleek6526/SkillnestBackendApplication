package com.skillnest.employerservice.feign;

import com.skillnest.employerservice.dtos.JobDTO;
import com.skillnest.employerservice.dtos.request.PostJobRequest;
import com.skillnest.employerservice.dtos.response.PostJobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "JOB-SERVICE", url = "http://localhost:8070")
public interface JobInterface {
    @PostMapping("job-service/post-jobs")
    public ResponseEntity<PostJobResponse> postJob(@ModelAttribute PostJobRequest jobRequest);

}
