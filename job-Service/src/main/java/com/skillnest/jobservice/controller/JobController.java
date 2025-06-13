package com.skillnest.jobservice.controller;

import com.skillnest.jobservice.data.model.Job;
import com.skillnest.jobservice.data.repository.JobRepository;
import com.skillnest.jobservice.dtos.JobDTO;
import com.skillnest.jobservice.dtos.request.*;
import com.skillnest.jobservice.dtos.response.JobResponse;
import com.skillnest.jobservice.dtos.response.PostJobResponse;
import com.skillnest.jobservice.dtos.response.TakeJobResponse;
import com.skillnest.jobservice.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("job-service")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final JobRepository jobRepository;

    @PostMapping("post-jobs")
    public ResponseEntity<PostJobResponse> postJob(@ModelAttribute PostJobRequest jobRequest) {
        PostJobResponse response = jobService.postJobs(jobRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("get-all-jobs")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> responses = jobService.getAllJobs();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable("id") String id) {
        JobResponse response = jobService.getJobById(id);
        Optional<Job> existingJob = jobRepository.findById(response.getJob().getId());
        if(existingJob.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Job job = existingJob.get();
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/take")
    public ResponseEntity<TakeJobResponse> takeJob(@RequestBody TakeJobRequest takeJobRequest) {
        TakeJobResponse response = jobService.takeJob(takeJobRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<JobResponse> updateJob(@RequestBody UpdateJobRequest updateJobRequest) {
        JobResponse response = jobService.updateJob(updateJobRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobResponse> deleteJob(@PathVariable String id) {
        JobResponse response = jobService.deleteJob(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/status")
    public ResponseEntity<JobResponse> changeJobStatus(@RequestBody ChangeJobStatusRequest changeJobStatusRequest) {
        JobResponse response = jobService.changeJobStatus(changeJobStatusRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/negotiate-payment")
    public ResponseEntity<JobResponse> negotiatePayment(@RequestBody NegotiatedPaymentRequest negotiatedPaymentRequest) {
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
