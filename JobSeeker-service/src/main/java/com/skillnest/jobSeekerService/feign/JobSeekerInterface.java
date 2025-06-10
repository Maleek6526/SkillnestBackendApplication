package com.skillnest.jobSeekerService.feign;

import com.skillnest.jobSeekerService.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USERSERVICE", url = "http://localhost:8080")
public interface JobSeekerInterface {
    @GetMapping("/api/skill-nest/auth/users/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable String userId);

}
