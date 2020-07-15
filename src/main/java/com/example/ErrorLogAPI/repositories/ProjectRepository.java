package com.example.ErrorLogAPI.repositories;

import com.example.ErrorLogAPI.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, String> {
    public Optional<Project> findByURL(String URL);
}
