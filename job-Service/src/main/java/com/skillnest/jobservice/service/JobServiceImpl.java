package com.skillnest.jobservice.service;

import com.skillnest.jobservice.data.enums.JobStatus;
import com.skillnest.jobservice.data.model.Job;
import com.skillnest.jobservice.data.repository.JobRepository;
import com.skillnest.jobservice.dtos.request.*;
import com.skillnest.jobservice.dtos.response.JobResponse;
import com.skillnest.jobservice.dtos.response.PostJobResponse;
import com.skillnest.jobservice.dtos.response.TakeJobResponse;
import com.skillnest.jobservice.exception.EmployerNotFoundException;
import com.skillnest.jobservice.exception.JobNotFoundException;
import com.skillnest.jobservice.exception.JobNotOpenException;
import com.skillnest.jobservice.exception.JobSeekerNotFoundException;
import com.skillnest.jobservice.feign.EmployerInterface;
import com.skillnest.jobservice.feign.JobSeekerInterface;
import com.skillnest.jobservice.mapper.JobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final WorkImageService workImageService;
    private final JobSeekerInterface jobSeekerInterface;
    private final EmployerInterface employerInterface;

    @Override
    public PostJobResponse postJobs(PostJobRequest jobRequest){

        Job job = JobMapper.mapToJob(jobRequest);
        job.setStatus(JobStatus.OPEN);
        job.setPostedDate(LocalDateTime.now());
        PostJobResponse postJobResponse = employerInterface.postJob(jobRequest);
        job.setEmployerId(postJobResponse.getEmployerId());
        job.setWorkImage(workImageService.uploadImage(jobRequest.getJobImages()).getWorkImage());
        jobRepository.save(job);
        return JobMapper.mapToPostJobResponse("Job Posted successfully",job.getEmployerId(), job.getId());
    }
    @Override
    public List<JobResponse> getAllJobs(){
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(job -> JobMapper.mapToJobResponse("All jobs retrieved successfully",job))
                .collect(Collectors.toList());
    }
    @Override
    public JobResponse getJobById(String id){
        Optional<Job> job = jobRepository.findById(id);
        if(job.isEmpty()){
            throw new JobNotFoundException("Job nt found");
        }
        return JobMapper.mapToJobResponse("Job retrieved successfully",job.get());
    }
    @Override
    public TakeJobResponse takeJob(TakeJobRequest jobRequest){
        Optional<Job> job = jobRepository.findById(jobRequest.getJobId());
        if(job.isEmpty()){
            throw new JobNotFoundException("Job not found");
        }
        if (job.get().getStatus() != JobStatus.OPEN){
            throw new JobNotOpenException("Job not open");
        }
        TakeJobResponse takeJobResponse = jobSeekerInterface.takeJob(jobRequest);
        if(takeJobResponse.getJobSeekerId() == null){
            throw new JobSeekerNotFoundException("JobSeeker Not Found");
        }
        job.get().setJobSeekerId(takeJobResponse.getJobSeekerId());
        job.get().setStatus(JobStatus.TAKEN);
        job.get().setLastUpdatedDate(LocalDateTime.now());

        jobRepository.save(job.get());
        return  JobMapper.mapToTakeJobResponse("Job taken successfully",  takeJobResponse.getJobSeekerId(), job.get().getId());
    }
    @Override
    public JobResponse updateJob(UpdateJobRequest jobRequest){
        Optional<Job> job = jobRepository.findById(jobRequest.getJobId());
        if(job.isEmpty()){
            throw new JobNotFoundException("Job not found");
        }
        if (job.get().getStatus() != JobStatus.OPEN){
            throw new JobNotOpenException("Job not open");
        }
        JobMapper.mapToUpdateJobRequest(job.get(),jobRequest);
        job.get().setWorkImage(workImageService.uploadImage(jobRequest.getWorkImage()).getWorkImage());
        jobRepository.save(job.get());
        return JobMapper.mapToJobResponse("Job updated successfully",job.get());
    }
    @Override
    public JobResponse deleteJob(String id){
        Optional<Job> job = jobRepository.findById(id);
        if(job.isEmpty()){
            throw new JobNotFoundException("Job not found");
        }
        job.get().setWorkImage(workImageService.deleteImage(job.get().getWorkImage().getId()).getWorkImage());
        jobRepository.delete(job.get());
        return JobMapper.mapToJobResponse("Job deleted successfully",null);
    }
    @Override
    public JobResponse changeJobStatus(ChangeJobStatusRequest jobRequest){
        Optional<Job> job = jobRepository.findById(jobRequest.getJobId());
        if(job.isEmpty()){
            throw new JobNotFoundException("Job not found");
        }
        job.get().setStatus(jobRequest.getStatus());
        job.get().setLastUpdatedDate(LocalDateTime.now());
        jobRepository.save(job.get());
        return JobMapper.mapToJobResponse("Job updated successfully",job.get());
    }
    @Override
    public JobResponse negotiatedPayment(NegotiatedPaymentRequest jobRequest){
        Optional<Job> job = jobRepository.findById(jobRequest.getJobId());
        if(job.isEmpty()){
            throw new JobNotFoundException("Job not found");
        }
        job.get().setNegotiatedPayment(jobRequest.getNegotiatedPaymentAmount());
        job.get().setLastUpdatedDate(LocalDateTime.now());
        jobRepository.save(job.get());
        return JobMapper.mapToJobResponse("Payment has been set",job.get());
    }
    @Override
    public JobResponse verifyCompletion(VerifyCompleteJobRequest jobRequest){
        Optional<Job> job = jobRepository.findById(jobRequest.getJobId());
        if(job.isEmpty()){
            throw new JobNotFoundException("Job not found");
        }
        if(jobRequest.isJobSeeker()){
            jobSeekerCompletion(jobRequest, job);
        }else{
            employerCompletion(jobRequest, job);
        }
        job.get().setLastUpdatedDate(LocalDateTime.now());
        if (job.get().isJobSeekerVerifiedCompletion() && job.get().isEmployerVerifiedCompletion()) {
            job.get().setStatus(JobStatus.VERIFIED_COMPLETION);
        }
        if (job.get().isJobSeekerVerifiedCompletion() && job.get().isEmployerVerifiedCompletion()) {
            job.get().setStatus(JobStatus.VERIFIED_COMPLETION);
        }
        jobRepository.save(job.get());
        return JobMapper.mapToJobResponse("Job verified successfully",job.get());
    }

    @Override
    public JobResponse completeJob(CompleteJobRequest jobRequest){
        Job job = jobRepository.findById(jobRequest.getJobId())
                .orElseThrow(() -> new JobNotFoundException("Job not found with ID: " + jobRequest.getJobId()));
        if (job.getStatus() != JobStatus.IN_PROGRESS && job.getStatus() != JobStatus.TAKEN) {
            throw new IllegalStateException("Job is can't be marked as completion: " + job.getStatus());
        }

        if (jobRequest.isJobSeeker()) {
            if (job.getJobSeekerId() == null || !job.getJobSeekerId().equals(jobRequest.getUserId())) {
                throw new JobSeekerNotFoundException("JobSeeker not assigned to job " + jobRequest.getJobId());
            }
            job.setStatus(JobStatus.COMPLETED_BY_JOB_SEEKER);
        } else {
            if (job.getEmployerId() == null || !job.getEmployerId().equals(jobRequest.getUserId())) {
                throw new EmployerNotFoundException("Employer not found for job " + jobRequest.getJobId());
            }
            job.setStatus(JobStatus.COMPLETED_BY_EMPLOYER);
        }
        job.setLastUpdatedDate(LocalDateTime.now());
        jobRepository.save(job);
        return JobMapper.mapToJobResponse("Job marked as completed successfully by " + (jobRequest.isJobSeeker() ? "jobseeker" : "employer"), job);

    }
    @Override
    public List<Job> findBySalaryRange(BigDecimal min, BigDecimal max){
        if (min != null && max != null) {
            return jobRepository.findByProposedPaymentBetween(min, max);
        } else if (min != null) {
            return jobRepository.findByProposedPaymentGreaterThanEqual(min);
        } else if (max != null) {
            return jobRepository.findByProposedPaymentLessThanEqual(max);
        } else {
            return jobRepository.findAll();
        }
    }
    @Override
    public List<Job> findByJobType(String jobType){
        return jobRepository.findAllByJobType(jobType);
    }

    @Override
    public List<Job> findByLocation(String location){
        return jobRepository.findAllByLocation(location);
    }

    private static void employerCompletion(VerifyCompleteJobRequest jobRequest, Optional<Job> job) {
        if(job.isEmpty()){
            throw new JobNotFoundException("job not found");
        }
        if(job.get().getEmployerId().isEmpty()){
            throw new EmployerNotFoundException("employerId is empty");
        }
        if (!job.get().getEmployerId().equals(jobRequest.getUserId())){
            throw new EmployerNotFoundException("employerId is empty");
        }
        job.get().setEmployerVerifiedCompletion(true);
        if(job.get().getStatus() == JobStatus.IN_PROGRESS || job.get().getStatus() == JobStatus.TAKEN){
            job.get().setStatus(JobStatus.COMPLETED_BY_EMPLOYER);
        }
    }

    private static void jobSeekerCompletion(VerifyCompleteJobRequest jobRequest, Optional<Job> job) {
        if(job.isEmpty()){
            throw new JobNotFoundException("job not found");
        }
        if(job.get().getJobSeekerId().isEmpty()){
            throw new JobSeekerNotFoundException("Job seekerId is empty");
        }
        if (!job.get().getJobSeekerId().equals(jobRequest.getUserId())){
            throw new JobSeekerNotFoundException("Job seekerId is empty");
        }
        job.get().setJobSeekerVerifiedCompletion(true);
        if(job.get().getStatus() == JobStatus.IN_PROGRESS || job.get().getStatus() == JobStatus.TAKEN){
            job.get().setStatus(JobStatus.COMPLETED_BY_JOB_SEEKER);
        }
    }


}
