package com.skillnest.userservice.dtos.request;

import com.skillnest.userservice.data.enums.Role;
import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String token;
    private String role;
}
