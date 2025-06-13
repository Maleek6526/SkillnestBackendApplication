package com.skillnest.employerservice.data.repository;

import com.skillnest.employerservice.data.model.Employer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends MongoRepository<Employer, String> {
}
