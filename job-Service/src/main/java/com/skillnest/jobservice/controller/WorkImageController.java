package com.skillnest.jobservice.controller;

import com.skillnest.jobservice.dtos.response.WorkImageResponse;
import com.skillnest.jobservice.service.WorkImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("images")
@RequiredArgsConstructor
public class WorkImageController {

    private final WorkImageService workImageService;

    @PostMapping("/upload")
    public ResponseEntity<WorkImageResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        WorkImageResponse response = workImageService.uploadImage(file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<WorkImageResponse> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        WorkImageResponse response = workImageService.uploadImages(files);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<WorkImageResponse> getImage(@PathVariable String imageId) {
        WorkImageResponse response = workImageService.getImage(imageId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<WorkImageResponse> deleteImage(@PathVariable String imageId) {
        WorkImageResponse response = workImageService.deleteImage(imageId);
        return ResponseEntity.ok(response);
    }
}
