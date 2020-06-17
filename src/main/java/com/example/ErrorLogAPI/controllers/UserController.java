package com.example.ErrorLogAPI.controllers;

import com.example.ErrorLogAPI.models.User;
import com.example.ErrorLogAPI.repositories.UserRepository;
import com.example.ErrorLogAPI.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class UserController {

    UserService userService;

    public UserController(UserRepository userRepository) {
        this.userService = new UserService(userRepository);
    }

    // Gets all the users in the database
    @GetMapping("/users")
    public List<User> getUsers() {
        return this.userService.getUsers();
    }

    // Gets a specific user from the database
    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable String id) {
        return this.userService.getUser(id);
    }

    // Adds a user to the database
    @PostMapping("/users")
    public User addNewUser(@RequestBody User u) {
        return this.userService.addNewUser(u);
    }

    //updates the user from the database
    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User u, @PathVariable String id) {
        return this.userService.updateUser(u, id);
    }

}
