package com.skillnest.jobservice.service;


import com.skillnest.jobservice.data.model.Job;
import com.skillnest.jobservice.dtos.request.*;
import com.skillnest.jobservice.dtos.response.JobResponse;
import com.skillnest.jobservice.dtos.response.PostJobResponse;
import com.skillnest.jobservice.dtos.response.TakeJobResponse;

import java.math.BigDecimal;
import java.util.List;

public interface JobService {

    PostJobResponse postJobs(PostJobRequest jobRequest);

    List<JobResponse> getAllJobs();

    JobResponse getJobById(String id);

    TakeJobResponse takeJob(TakeJobRequest jobRequest);

    JobResponse updateJob(UpdateJobRequest jobRequest);

    JobResponse deleteJob(String id);

    JobResponse changeJobStatus(ChangeJobStatusRequest jobRequest);

    JobResponse negotiatedPayment(NegotiatedPaymentRequest jobRequest);

    JobResponse verifyCompletion(VerifyCompleteJobRequest jobRequest);

    JobResponse completeJob(CompleteJobRequest jobRequest);

    List<Job> findBySalaryRange(BigDecimal min, BigDecimal max);

    List<Job> findByJobType(String jobType);

    List<Job> findByLocation(String location);
}
