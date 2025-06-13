package com.skillnest.employerservice.data.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Document("employer")
public class Employer {
    @Id
    private String id;
    private String location;
    private String fullName;
    private String phoneNumber;
    private String profilePictureUrl;
    private String bio;
    private String documentUrl;
    private String companyName;
    private String companyDescription;
    
}
