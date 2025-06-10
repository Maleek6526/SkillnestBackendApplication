package com.skillnest.jobSeekerService.data.repository;

import com.skillnest.jobSeekerService.data.model.VerificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerificationDocumentRepository extends MongoRepository<VerificationDocument, String> {
    Optional<VerificationDocument> findByJobSeekerId(String jobSeekerId);
}
