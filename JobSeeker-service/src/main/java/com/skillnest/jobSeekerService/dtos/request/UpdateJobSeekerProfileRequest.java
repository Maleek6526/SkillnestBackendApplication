package com.skillnest.jobSeekerService.dtos.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateJobSeekerProfileRequest {
    private String userId;
    private String fullName;
    private List<String> skills;
    private MultipartFile resume;
    private String location;
    private String phoneNumber;
    private MultipartFile profilePicture;
    private MultipartFile document;

}
