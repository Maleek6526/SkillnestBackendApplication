package com.skillnest.jobservice.controller;

import com.skillnest.jobservice.dtos.request.*;
import com.skillnest.jobservice.dtos.response.JobResponse;
import com.skillnest.jobservice.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("job-service")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping("post-jobs")
    public ResponseEntity<JobResponse> postJob(@Valid @RequestBody JobRequest jobRequest) {
        JobResponse response = jobService.postJobs(jobRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("get-all-jobs")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> responses = jobService.getAllJobs();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable String id) {
        JobResponse response = jobService.getJobById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/take")
    public ResponseEntity<JobResponse> takeJob(@Valid @RequestBody TakeJobRequest takeJobRequest) {
        JobResponse response = jobService.takeJob(takeJobRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<JobResponse> updateJob(@Valid @RequestBody UpdateJobRequest updateJobRequest) {
        JobResponse response = jobService.updateJob(updateJobRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobResponse> deleteJob(@PathVariable String id) {
        JobResponse response = jobService.deleteJob(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/status")
    public ResponseEntity<JobResponse> changeJobStatus(@Valid @RequestBody ChangeJobStatusRequest changeJobStatusRequest) {
        JobResponse response = jobService.changeJobStatus(changeJobStatusRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/negotiate-payment")
    public ResponseEntity<JobResponse> negotiatePayment(@Valid @RequestBody NegotiatedPaymentRequest negotiatedPaymentRequest) {
        JobResponse response = jobService.negotiatedPayment(negotiatedPaymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/verify-completion")
    public ResponseEntity<JobResponse> verifyCompletion(@Valid @RequestBody VerifyCompleteJobRequest verifyCompleteJobRequest) {
        JobResponse response = jobService.verifyCompletion(verifyCompleteJobRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/complete")
    public ResponseEntity<JobResponse> completeJob(@Valid @RequestBody CompleteJobRequest completeJobRequest) {
        JobResponse response = jobService.completeJob(completeJobRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
