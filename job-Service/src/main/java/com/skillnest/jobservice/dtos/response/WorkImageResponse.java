package com.skillnest.jobservice.dtos.response;

import com.skillnest.jobservice.data.model.WorkImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkImageResponse {
   private String message;
   private WorkImage workImage;
}
