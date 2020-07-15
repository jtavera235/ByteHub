package com.example.ErrorLogAPI.controllers;

import com.example.ErrorLogAPI.models.User;
import com.example.ErrorLogAPI.repositories.ErrorRepository;
import com.example.ErrorLogAPI.repositories.PasswordRepository;
import com.example.ErrorLogAPI.repositories.ProjectRepository;
import com.example.ErrorLogAPI.repositories.UserRepository;
import com.example.ErrorLogAPI.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
public class UserController {

    UserService userService;


    public UserController(UserRepository userRepository, ProjectRepository projectRepository,
                          ErrorRepository errorRepository, PasswordRepository passwordRepository) {
        this.userService = new UserService(userRepository, projectRepository, errorRepository, passwordRepository);
    }

    // Gets all the users in the database
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return this.userService.getUsers();
    }

    // Gets a specific user from the database
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        /*
        String id -> The id of the user
         */
        Optional<User> user = this.userService.getUser(id);
        if (user.isPresent()) {
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    /* This is depreciated and switched to the authentication service    */
    // Adds a new user to the database
    @PostMapping("/users")
    public ResponseEntity<User> addNewUser(@RequestBody User u) {
        return this.userService.addNewUser(u);
    }


    // updates the user's account data
    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User u, @PathVariable String id) {
        /*
        User u -> The updated user object
        String id -> the ID of the user that is getting updated
         */
        return this.userService.updateUser(u, id);
    }

    // deletes the passed user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        /*
        String id -> The ID of the user
         */
        return this.userService.deleteUser(id);
    }

    // user accepted project invite
    @PostMapping("/users/{id}/projects/{projectId}")
    public ResponseEntity<User> acceptedProject(@PathVariable String id, @PathVariable String projectId) {
        /*
        String id -> The id of the user
        String projectID -> The id of the project
         */
        Optional<User> u = this.userService.acceptedProject(id, projectId);
        if (u.isPresent()) {
            return new ResponseEntity<>(u.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
