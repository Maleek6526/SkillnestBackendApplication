package com.skillnest.jobservice.data.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("work_image")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkImage {
    @Id
    private String id;
    private String imageUrl;
    private String caption;
    @CreatedDate
    private LocalDateTime uploadDate;
}
