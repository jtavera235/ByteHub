package com.example.ErrorLogAPI.repositories;

import com.example.ErrorLogAPI.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    public Optional<User> findByEmail(String email);
}
