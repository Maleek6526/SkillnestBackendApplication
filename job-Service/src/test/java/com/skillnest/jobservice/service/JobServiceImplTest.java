package com.skillnest.jobservice.service;

import com.skillnest.jobservice.data.enums.JobStatus;
import com.skillnest.jobservice.data.model.Job;
import com.skillnest.jobservice.data.repository.JobRepository;
import com.skillnest.jobservice.dtos.request.*;
import com.skillnest.jobservice.dtos.response.JobResponse;
import com.skillnest.jobservice.dtos.response.PostJobResponse;
import com.skillnest.jobservice.dtos.response.TakeJobResponse;
import com.skillnest.jobservice.exception.JobNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class JobServiceImplTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private WorkImageService workImageService;

    MockMultipartFile mockFile;


    @BeforeEach
    public void setUp() throws IOException {
        byte[] imageBytes = Files.readAllBytes(Paths.get("src/test/resources/test-image.png"));
        mockFile = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", imageBytes);


    }
    @Test
    void postJobs_shouldSaveAndReturnJobResponse() {
        PostJobRequest request = new PostJobRequest();
        request.setJobType("plumbing");
        request.setTitle("lum");
        request.setJobImages(mockFile);

        PostJobResponse response = jobService.postJobs(request);
        assertNotNull(response);
        assertEquals("Job Posted successfully", response.getMessage());
    }

    @Test
    void getAllJobs_shouldReturnListOfJobResponses() {
        PostJobRequest request = new PostJobRequest();
        request.setJobType("plumbing");
        request.setTitle("lum");
        request.setJobImages(mockFile);


        PostJobRequest request2 = new PostJobRequest();
        request2.setJobType("plumbing");
        request2.setTitle("lum1");
        request2.setJobImages(mockFile);


        PostJobResponse response2 = jobService.postJobs(request2);
        PostJobResponse response = jobService.postJobs(request);

        List<JobResponse> responses = jobService.getAllJobs();

        assertEquals(2, responses.size());
        assertNotNull(response);
        assertNotNull(response2);
    }

    @Test
    void getJobById_shouldReturnJobResponse() {
        PostJobRequest request = new PostJobRequest();
        request.setJobType("plumbing");
        request.setTitle("lum");
        request.setJobImages(mockFile);


        PostJobResponse response = jobService.postJobs(request);
        JobResponse response2 = jobService.getJobById(response.getJobId());

        assertNotNull(response);
        assertNotNull(response2);
    }

    @Test
    void getJobById_shouldThrowExceptionIfNotFound() {
        assertThrows(JobNotFoundException.class, () -> jobService.getJobById("1"));
    }

    @Test
    void takeJob_shouldUpdateAndReturnJobResponse() {
        PostJobRequest request2 = new PostJobRequest();
        request2.setJobType("plumbing");
        request2.setTitle("lum");
        request2.setJobImages(mockFile);


        PostJobResponse response1 = jobService.postJobs(request2);
        TakeJobRequest request = new TakeJobRequest();
        request.setJobId(response1.getJobId());
        request.setJobSeekerId(response1.getJobId());
        TakeJobResponse response = jobService.takeJob(request);

        assertEquals("Job taken successfully", response.getMessage());
        assertNotNull(jobRepository.findById(response.getJobId()));
    }

    @Test
    void updateJob_shouldUpdateJobAndReturnResponse() {
        PostJobRequest request1 = new PostJobRequest();
        request1.setJobType("plumbing");
        request1.setTitle("lum");
        request1.setJobImages(mockFile);


        PostJobResponse response1 = jobService.postJobs(request1);

        UpdateJobRequest request = new UpdateJobRequest();
        request.setJobId(response1.getJobId());
        request.setTitle("updated");
        request.setWorkImage(mockFile);

        JobResponse response = jobService.updateJob(request);

        assertEquals("Job updated successfully", response.getMessage());
    }

    @Test
    void deleteJob_shouldDeleteJob() {
        PostJobRequest request = new PostJobRequest();
        request.setJobType("plumbing");
        request.setTitle("lum");
        request.setJobImages(mockFile);


        PostJobResponse response1 = jobService.postJobs(request);

        JobResponse response = jobService.deleteJob(response1.getJobId());

        assertEquals("Job deleted successfully", response.getMessage());
    }

    @Test
    void changeJobStatus_shouldUpdateStatus() {
        PostJobRequest request1 = new PostJobRequest();
        request1.setJobType("plumbing");
        request1.setTitle("lum");
        request1.setJobImages(mockFile);


        PostJobResponse response1 = jobService.postJobs(request1);
        ChangeJobStatusRequest request = new ChangeJobStatusRequest();
        request.setJobId(response1.getJobId());
        request.setStatus(JobStatus.TAKEN);

        JobResponse response = jobService.changeJobStatus(request);

        assertEquals("Job updated successfully", response.getMessage());
    }

    @Test
    void negotiatedPayment_shouldSetPayment() {
        PostJobRequest request1 = new PostJobRequest();
        request1.setJobType("plumbing");
        request1.setTitle("lum");
        request1.setJobImages(mockFile);

        PostJobResponse response1 = jobService.postJobs(request1);

        NegotiatedPaymentRequest request = new NegotiatedPaymentRequest();
        request.setJobId(response1.getJobId());
        request.setNegotiatedPaymentAmount(BigDecimal.valueOf(5000.0));
        JobResponse response = jobService.negotiatedPayment(request);

        assertEquals("Payment has been set", response.getMessage());
    }

    @Test
    void completeJob_shouldCompleteByJobSeeker() {
        Job job = new Job();
        job.setJobSeekerId("123");
        jobRepository.save(job);

        PostJobRequest request1 = new PostJobRequest();
        request1.setJobType("plumbing");
        request1.setTitle("lum");
        request1.setEmployerId("12");
        request1.setJobImages(mockFile);


        PostJobResponse response1 = jobService.postJobs(request1);

        TakeJobRequest request3 = new TakeJobRequest();
        request3.setJobId(response1.getJobId());
        request3.setJobSeekerId("123");

        TakeJobResponse response3 = jobService.takeJob(request3);

        ChangeJobStatusRequest request2 = new ChangeJobStatusRequest();
        request2.setJobId(response1.getJobId());
        request2.setStatus(JobStatus.TAKEN);

        JobResponse response = jobService.changeJobStatus(request2);
        CompleteJobRequest request = new CompleteJobRequest();
        request.setJobId(response1.getJobId());
        request.setUserId("123");
        request.setJobSeeker(true);

        JobResponse response2 = jobService.completeJob(request);

        assertEquals("Job marked as completed successfully by jobseeker", response2.getMessage());
    }

    @Test
    void completeJob_shouldCompleteByEmployer() {
        PostJobRequest request1 = new PostJobRequest();
        request1.setJobType("plumbing");
        request1.setTitle("lum");
        request1.setEmployerId("1");
        request1.setJobImages(mockFile);

        PostJobResponse response1 = jobService.postJobs(request1);
        ChangeJobStatusRequest request2 = new ChangeJobStatusRequest();
        request2.setJobId(response1.getJobId());
        request2.setStatus(JobStatus.TAKEN);

        JobResponse response12 = jobService.changeJobStatus(request2);

        CompleteJobRequest request = new CompleteJobRequest();
        request.setUserId("1");
        request.setJobId(response1.getJobId());
        request.setJobSeeker(false);
        JobResponse response = jobService.completeJob(request);

        assertEquals("Job marked as completed successfully by employer", response.getMessage());
    }

    @Test
    void verifyCompletion_shouldVerifyBoth() {
        Job job = new Job();
        job.setJobSeekerId("123");
        jobRepository.save(job);
        PostJobRequest request1 = new PostJobRequest();
        request1.setJobType("plumbing");
        request1.setTitle("lum");
        request1.setJobImages(mockFile);



        PostJobResponse response1 = jobService.postJobs(request1);


        TakeJobRequest request3 = new TakeJobRequest();
        request3.setJobId(response1.getJobId());
        request3.setJobSeekerId("123");

        TakeJobResponse response3 = jobService.takeJob(request3);
        VerifyCompleteJobRequest request = new VerifyCompleteJobRequest();
        request.setJobId(response1.getJobId());
        request.setUserId("123");
        request.setJobSeeker(true);
        JobResponse response = jobService.verifyCompletion(request);

        assertNotNull(response);
    }
    @AfterEach
    public void tearDown(){
        jobRepository.deleteAll();
    }
}