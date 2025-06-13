package com.skillnest.employerservice.feign;

import com.skillnest.employerservice.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USERSERVICE", url = "http://localhost:8080")
public interface EmployerInterface {
    @GetMapping("/api/skill-nest/auth/users/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable String userId);

}
