package com.skillnest.employerservice.data.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("employer")
@Builder
public class Employer {
    
}
