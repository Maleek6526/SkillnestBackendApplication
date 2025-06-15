package com.skillnest.jobservice.feign;

import com.skillnest.jobservice.dtos.request.PostJobRequest;
import com.skillnest.jobservice.dtos.response.PostJobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "EMPLOYER-SERVICE", url = "http://localhost:8081")
public interface EmployerInterface {
    @PostMapping("job-service/post-jobs")
    PostJobResponse postJob(PostJobRequest jobRequest);

}
