package com.skillnest.jobSeekerService.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("JOB-SERVICE")
public interface JobInterface {

}
