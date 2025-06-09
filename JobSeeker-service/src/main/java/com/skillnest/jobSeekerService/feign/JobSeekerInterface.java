package com.skillnest.jobSeekerService.feign;

import com.skillnest.jobSeekerService.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USERSERVICE")
public interface JobSeekerInterface {
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable String userId);

}
