package com.skillnest.jobSeekerService.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserResponse {
    private String message;
    private String userId;
    private String email;
}