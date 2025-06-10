package com.skillnest.jobSeekerService.controller;

import com.skillnest.jobSeekerService.dtos.request.*;
import com.skillnest.jobSeekerService.dtos.response.*;
import com.skillnest.jobSeekerService.service.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("job-seeker")
@RequiredArgsConstructor
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;
    @PostMapping("complete-profile")
    public ResponseEntity<RegisterJobSeekerResponse> completeProfile(@ModelAttribute RegisterJobSeekerRequest request){
        return ResponseEntity.ok(jobSeekerService.completeProfile(request));
    }
    @PostMapping("update-profile")
    public ResponseEntity<UpdateJobSeekerProfileResponse> updateProfile(@RequestBody UpdateJobSeekerProfileRequest request){
        return ResponseEntity.ok(jobSeekerService.updateProfile(request));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UpdateJobSeekerProfileResponse> getProfile(@PathVariable String userId){
        return ResponseEntity.ok(jobSeekerService.getProfile(userId));
    }
    @PostMapping("set-availability")
    public ResponseEntity<AvailabilitySlotResponse> setAvailability(@RequestBody List<AvailabilitySlotRequest> request){
        return ResponseEntity.ok(jobSeekerService.setAvailability(request));
    }
    @PostMapping("upload-documents")
    public ResponseEntity<UploadDocumentsResponse> uploadDocuments(@RequestBody VerificationDocumentRequest request){
        return ResponseEntity.ok(jobSeekerService.uploadDocuments(request));
    }
    @GetMapping("/{id}/documents")
    public ResponseEntity<VerificationDocumentResponse> getDocuments(@PathVariable String id){
        return ResponseEntity.ok(jobSeekerService.getDocuments(id));
    }
    @GetMapping("/{id}/availability")
    public ResponseEntity<List<AvailabilitySlotResponse>> getAvailability(@PathVariable String id){
        return ResponseEntity.ok(jobSeekerService.getAvailability(id));
    }
    @PostMapping("take")
    public ResponseEntity<TakeJobResponse> takeJob(@RequestBody TakeJobRequest request){
        return ResponseEntity.ok(jobSeekerService.takeJob(request));
    }
}
