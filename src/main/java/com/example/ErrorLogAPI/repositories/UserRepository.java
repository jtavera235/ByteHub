package com.example.ErrorLogAPI.repositories;

import com.example.ErrorLogAPI.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
public interface UserRepository extends MongoRepository<User, String> {

}
