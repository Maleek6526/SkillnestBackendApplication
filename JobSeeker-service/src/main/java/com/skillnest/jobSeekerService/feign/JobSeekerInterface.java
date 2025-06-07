package com.skillnest.jobSeekerService.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("USERSERVICE")
public class JobSeekerInterface {
}
