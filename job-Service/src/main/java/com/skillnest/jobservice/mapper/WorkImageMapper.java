package com.skillnest.jobservice.mapper;

import com.skillnest.jobservice.data.model.WorkImage;
import com.skillnest.jobservice.dtos.response.WorkImageResponse;

import java.util.List;

public class WorkImageMapper {

    public static WorkImageResponse mapToUploadImagesResponse(String message, WorkImage imageUrls) {
        WorkImageResponse response = new WorkImageResponse();
        response.setMessage(message);
        response.setWorkImage(imageUrls);
        return response;
    }
}
