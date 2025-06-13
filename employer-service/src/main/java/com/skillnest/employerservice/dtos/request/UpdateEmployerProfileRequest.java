package com.skillnest.employerservice.dtos.request;

import lombok.Data;

@Data
public class UpdateEmployerProfileRequest {
    private String userId;
    private String fullName;
    private String location;
    private String phoneNumber;
    private String profilePictureUrl;
}
