package com.skillnest.employerservice.controller;

import com.skillnest.employerservice.dtos.request.PostJobRequest;
import com.skillnest.employerservice.dtos.request.RegisterEmployerRequest;
import com.skillnest.employerservice.dtos.request.UpdateEmployerProfileRequest;
import com.skillnest.employerservice.dtos.response.PostJobResponse;
import com.skillnest.employerservice.dtos.response.RegisterEmployerResponse;
import com.skillnest.employerservice.dtos.response.UpdateEmployerProfileResponse;
import com.skillnest.employerservice.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("employer")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerService employerService;

    @PostMapping("complete-profile")
    public ResponseEntity<RegisterEmployerResponse> completeProfile(@ModelAttribute RegisterEmployerRequest request){
        return ResponseEntity.ok(employerService.completeProfile(request));
    }
    @PostMapping("update-profile")
    public ResponseEntity<UpdateEmployerProfileResponse> updateProfile(@RequestBody UpdateEmployerProfileRequest request){
        return ResponseEntity.ok(employerService.updateProfile(request));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UpdateEmployerProfileResponse> getProfile(@PathVariable String userId){
        return ResponseEntity.ok(employerService.getProfile(userId));
    }
    @PostMapping("post-job")
    public ResponseEntity<PostJobResponse> postJob(@RequestBody PostJobRequest request){
        return ResponseEntity.ok(employerService.postJob(request));
    }
}
