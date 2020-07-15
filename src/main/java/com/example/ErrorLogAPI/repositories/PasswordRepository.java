package com.example.ErrorLogAPI.repositories;

import com.example.ErrorLogAPI.models.Password;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordRepository extends MongoRepository<Password, String> {
    // deletes a password based on the passed email
    public void deleteByEmail(String email);

    // finds a password based on the passed email
    public Optional<Password> findByEmail(String email);
}
