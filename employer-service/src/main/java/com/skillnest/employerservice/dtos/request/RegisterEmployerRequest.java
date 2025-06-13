package com.skillnest.employerservice.dtos.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class RegisterEmployerRequest {
    private String email;
    private String userId;
    private String location;
    private String fullName;
    private String phoneNumber;
    private MultipartFile profilePictureUrl;
    private String bio;
    private MultipartFile documentUrl;
    private String companyName;
    private String companyDescription;
}
