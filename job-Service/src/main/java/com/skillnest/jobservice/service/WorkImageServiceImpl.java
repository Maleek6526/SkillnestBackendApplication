package com.skillnest.jobservice.service;

import com.cloudinary.Cloudinary;
import com.skillnest.jobservice.data.model.WorkImage;
import com.skillnest.jobservice.data.repository.WorkImageRepository;
import com.skillnest.jobservice.dtos.response.WorkImageResponse;
import com.skillnest.jobservice.exception.NoImageSelectedException;
import com.skillnest.jobservice.mapper.WorkImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkImageServiceImpl implements WorkImageService {

    private final WorkImageRepository workImageRepository;
    private final Cloudinary cloudinary;

    @Override
    public WorkImageResponse uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new NoImageSelectedException("No image selected");
        }

        try {
            String cloudUrl = getCloudUrl(file);

            WorkImage workImage = WorkImage.builder()
                    .id(UUID.randomUUID().toString())
                    .imageUrl(cloudUrl)
                    .uploadDate(LocalDateTime.now())
                    .caption(file.getOriginalFilename())
                    .build();

            workImageRepository.save(workImage);
            log.error("image not uploaded: {}", cloudUrl);
            log.info("Image uploaded: {}", cloudUrl);
            return WorkImageMapper.mapToUploadImagesResponse("Image uploaded successfully", workImage);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("Error uploading image: {}", e.getMessage());
            throw new NoImageSelectedException("Error uploading image");
        }
    }

    private String getCloudUrl(MultipartFile file) throws IOException {
        return cloudinary
                .uploader()
                .upload(file.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }

    @Override
    public WorkImageResponse getImage(String imageId) {
        WorkImage image = workImageRepository.findById(imageId)
                .orElseThrow(() -> new NoImageSelectedException("Image not found"));
        return WorkImageMapper.mapToUploadImagesResponse("Image retrieved successfully", image);
    }

    @Override
    public WorkImageResponse deleteImage(String imageId) {
        WorkImage image = workImageRepository.findById(imageId)
                .orElseThrow(() -> new NoImageSelectedException("Image not found"));

        workImageRepository.deleteById(imageId);

        log.info("Image deleted with ID: {}", imageId);
        return WorkImageMapper.mapToUploadImagesResponse("Image deleted successfully", image);
    }

    @Override
    public WorkImageResponse uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new NoImageSelectedException("No images selected");
        }

        List<WorkImage> savedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            cloudinaryUpload(savedImages, file);
        }

        if (savedImages.isEmpty()) {
            throw new NoImageSelectedException("No valid images were uploaded");
        }

        WorkImage lastImage = savedImages.getLast();
        return WorkImageMapper.mapToUploadImagesResponse("Images uploaded successfully", lastImage);
    }

    private void cloudinaryUpload(List<WorkImage> savedImages, MultipartFile file) {
        try {
            String cloudUrl = getCloudUrl(file);

            WorkImage workImage = WorkImage.builder()
                    .id(UUID.randomUUID().toString())
                    .imageUrl(cloudUrl)
                    .uploadDate(LocalDateTime.now())
                    .caption(file.getOriginalFilename())
                    .build();

            workImageRepository.save(workImage);
            savedImages.add(workImage);
        } catch (IOException e) {
            log.error("Error uploading one of the images: {}", e.getMessage());
        }
    }

}
