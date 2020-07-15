package com.example.ErrorLogAPI.repositories;

import com.example.ErrorLogAPI.models.Errors;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ErrorRepository extends MongoRepository<Errors, String> {

    // gets an error from the database based on the passed project ID
    public Errors findByProjectId(String projectId);
}
