package com.skillnest.jobSeekerService.dtos.request;

import com.skillnest.jobSeekerService.data.enums.Role;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegisterJobSeekerRequest {
    private String email;
    private String userId;
    private String location;
    private String resumeUrl;
    private String fullName;
    private String phoneNumber;
    private MultipartFile profilePictureUrl;
    private String bio;
    private List<String> skillIds;
    private String availabilitySlotIds;
    private MultipartFile documentUrl;
}

