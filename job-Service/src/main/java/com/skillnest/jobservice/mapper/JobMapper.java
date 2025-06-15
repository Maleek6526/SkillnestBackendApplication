package com.skillnest.jobservice.mapper;


import com.skillnest.jobservice.data.model.Job;
import com.skillnest.jobservice.data.model.WorkImage;
import com.skillnest.jobservice.dtos.JobDTO;
import com.skillnest.jobservice.dtos.request.JobRequest;
import com.skillnest.jobservice.dtos.request.PostJobRequest;
import com.skillnest.jobservice.dtos.request.UpdateJobRequest;
import com.skillnest.jobservice.dtos.response.JobResponse;
import com.skillnest.jobservice.dtos.response.PostJobResponse;
import com.skillnest.jobservice.dtos.response.TakeJobResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class JobMapper {
    public static Job mapToJob(PostJobRequest jobRequest){
        return Job.builder()
                .id(UUID.randomUUID().toString())
                .jobType(jobRequest.getJobType())
                .deadline(jobRequest.getDeadline())
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .contactInfo(jobRequest.getContactInfo())
                .requiredSkills(jobRequest.getRequiredSkillIds())
                .proposedPayment(jobRequest.getProposedPayment())
                .location(jobRequest.getLocation())
                .build();
    }
    public static JobResponse mapToJobResponse(String message, Job job){
        JobResponse jobResponse = new JobResponse();
        jobResponse.setJob(job);
        jobResponse.setMessage(message);
        return jobResponse;
    }
    public static void mapToUpdateJobRequest(Job job, UpdateJobRequest jobRequest){
        job.setJobType(jobRequest.getJobType());
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setLocation(jobRequest.getLocation());
        job.setRequiredSkills(jobRequest.getRequiredSkills());
        job.setProposedPayment(jobRequest.getProposedPayment());
        job.setStatus(jobRequest.getStatus());
        job.setDeadline(jobRequest.getDeadline());
        job.setLastUpdatedDate(LocalDateTime.now());
        job.setContactInfo(jobRequest.getContactInfo());
    }

    public static TakeJobResponse mapToTakeJobResponse(String jobTakenSuccessfully, String jobSeekerId, String jobId) {
        TakeJobResponse takeJobResponse = new TakeJobResponse();
        takeJobResponse.setJobId(jobId);
        takeJobResponse.setJobSeekerId(jobSeekerId);
        takeJobResponse.setMessage(jobTakenSuccessfully);
        return takeJobResponse;
    }
    public static PostJobResponse mapToPostJobResponse(String jobPosted, String id, String jobId) {
        PostJobResponse postJobResponse = new PostJobResponse();
        postJobResponse.setJobId(jobId);
        postJobResponse.setEmployerId(id);
        postJobResponse.setMessage(jobPosted);
        return postJobResponse;
    }
}
