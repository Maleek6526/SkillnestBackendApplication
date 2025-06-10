package com.skillnest.jobservice.feign;

import com.skillnest.jobservice.dtos.request.TakeJobRequest;
import com.skillnest.jobservice.dtos.response.TakeJobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "JOBSEEKER-SERVICE", url = "http://localhost:8090")
public interface JobSeekerInterface {
    @PostMapping("/job-seeker/take")
    TakeJobResponse takeJob(TakeJobRequest takeJobRequest);
}
