package com.skillnest.jobservice.service;

import com.skillnest.jobservice.dtos.response.WorkImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkImageService {

    WorkImageResponse uploadImage(MultipartFile file);
    WorkImageResponse getImage(String imageId);
    WorkImageResponse deleteImage(String imageId);
    WorkImageResponse uploadImages(List<MultipartFile> files);

}
