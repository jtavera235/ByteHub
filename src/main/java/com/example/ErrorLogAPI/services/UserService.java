package com.example.ErrorLogAPI.services;


import com.example.ErrorLogAPI.models.User;
import com.example.ErrorLogAPI.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    ArrayList<User> db = new ArrayList<User>();


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // gets all the users in the database
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Gets a specific user from the database
    public Optional<User> getUser(String id) {
        return this.userRepository.findById(id);
    }

    // Adds a user to the database
    public User addNewUser(User u) {
        return this.userRepository.save(u);
    }

    // updates a user
    public User updateUser(User u, String id) {
        return this.userRepository.findById(id).map(user -> {
            user.setName(u.getName());
            user.setAge(u.getAge());
            return this.userRepository.save(user);
        }).orElseGet(() -> {
            u.setId(id);
            return this.userRepository.save(u);
        });
    }
}
