package com.skillnest.jobSeekerService.dtos.request;

import com.skillnest.jobSeekerService.data.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String email;
}