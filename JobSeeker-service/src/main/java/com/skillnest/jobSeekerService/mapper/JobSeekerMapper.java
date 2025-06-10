package com.skillnest.jobSeekerService.mapper;

import com.skillnest.jobSeekerService.data.model.AvailabilitySlot;
import com.skillnest.jobSeekerService.data.model.JobSeeker;
import com.skillnest.jobSeekerService.data.model.VerificationDocument;
import com.skillnest.jobSeekerService.dtos.JobDTO;
import com.skillnest.jobSeekerService.dtos.UserDto;
import com.skillnest.jobSeekerService.dtos.request.*;
import com.skillnest.jobSeekerService.dtos.response.*;

import java.util.List;
import java.util.UUID;

public class JobSeekerMapper {

    public static JobSeeker mapToRegisterJobSeeker(UserDto user, RegisterJobSeekerRequest registerJobSeekerRequest){
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setId(UUID.randomUUID().toString());
        jobSeeker.setUserId(user.getId());
        jobSeeker.setAvailabilitySlotIds(registerJobSeekerRequest.getAvailabilitySlotIds());
        jobSeeker.setBio(registerJobSeekerRequest.getBio());
        jobSeeker.setFullName(registerJobSeekerRequest.getFullName());
        jobSeeker.setSkillIds(registerJobSeekerRequest.getSkillIds());
        jobSeeker.setPhoneNumber(registerJobSeekerRequest.getPhoneNumber());
        jobSeeker.setLocation(registerJobSeekerRequest.getLocation());
        return jobSeeker;
    }
    public static RegisterJobSeekerResponse mapToRegisterJobSeekerResponse(String message, JobSeeker jobSeeker){
        RegisterJobSeekerResponse response = new RegisterJobSeekerResponse();
        response.setMessage(message);
        response.setJobSeeker(jobSeeker);
        return response;
    }

    public static void mapToUpdateJobSeekerProfile(JobSeeker jobSeeker, UpdateJobSeekerProfileRequest request) {
        jobSeeker.setFullName(request.getFullName());
        jobSeeker.setLocation(request.getLocation());
        jobSeeker.setPhoneNumber(request.getPhoneNumber());
        jobSeeker.setProfilePictureUrl(request.getProfilePictureUrl());
        jobSeeker.setResumeUrl(request.getResumeUrl());
    }
    public static UpdateJobSeekerProfileResponse mapToUpdateJobSeekerProfileResponse(String message, JobSeeker jobSeeker){
        UpdateJobSeekerProfileResponse response = new UpdateJobSeekerProfileResponse();
        response.setMessage(message);
        response.setJobSeeker(jobSeeker);
        return response;
    }
    public static String mapToAvailabilitySlot(String jobSeekerId, List<AvailabilitySlotRequest> slots){
        AvailabilitySlot availabilitySlot = new AvailabilitySlot();
        availabilitySlot.setId(UUID.randomUUID().toString());
        availabilitySlot.setJobSeekerId(jobSeekerId);
        availabilitySlot.setEndTime(slots.getLast().getEndTime());
        availabilitySlot.setDayOfWeek(slots.getLast().getDayOfWeek());
        availabilitySlot.setStartTime(slots.getLast().getStartTime());
        availabilitySlot.setId(UUID.randomUUID().toString());
        return availabilitySlot.getId();
    }
    public static AvailabilitySlotResponse mapToAvailabilitySlotResponse(String message, AvailabilitySlot availabilitySlot){
        AvailabilitySlotResponse response = new AvailabilitySlotResponse();
        response.setAvailabilitySlot(availabilitySlot);
        response.setMessage(message);
        return response;
    }
    public static VerificationDocument mapToVerificationDocument(String jobSeekerId, VerificationDocumentRequest request){
        VerificationDocument verificationDocument = new VerificationDocument();
        verificationDocument.setId(UUID.randomUUID().toString());
        verificationDocument.setType(request.getType());
        verificationDocument.setStatus(request.getStatus());
        verificationDocument.setJobSeekerId(jobSeekerId);
        return verificationDocument;
    }
    public static VerificationDocumentResponse mapToVerificationDocumentResponse(String message, VerificationDocument document){
        VerificationDocumentResponse response = new VerificationDocumentResponse();
        response.setDocument(document);
        response.setMessage(message);
        return response;
    }


    public static UploadDocumentsResponse mapToUploadDocumentResponse(String message, VerificationDocument verificationDocument) {
        UploadDocumentsResponse response = new UploadDocumentsResponse();
        response.setDocument(verificationDocument);
        response.setMessage(message);
        return response;
    }

    public static TakeJobResponse mapToTakeJobResponse(String message, String jobSeekerId ){
        TakeJobResponse takeJobResponse = new TakeJobResponse();
        takeJobResponse.setMessage(message);
        takeJobResponse.setJobSeekerId(jobSeekerId);
        return takeJobResponse;
    }

    public static TakeJobRequest mapToTakeJobRequest(JobDTO job, TakeJobRequest request) {
        TakeJobRequest takeJobRequest = new TakeJobRequest();
        takeJobRequest.setJobId(job.getJobId());
        takeJobRequest.setJobSeekerId(request.getJobSeekerId());
        return takeJobRequest;
    }
}
