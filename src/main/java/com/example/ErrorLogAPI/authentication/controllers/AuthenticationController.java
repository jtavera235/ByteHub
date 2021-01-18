package com.example.ErrorLogAPI.authentication.controllers;


import com.example.ErrorLogAPI.authentication.services.AuthenticationService;
import com.example.ErrorLogAPI.authentication.models.LoginRequest;
import com.example.ErrorLogAPI.authentication.models.LoginResponse;
import com.example.ErrorLogAPI.models.User;
import com.example.ErrorLogAPI.repositories.ErrorRepository;
import com.example.ErrorLogAPI.repositories.PasswordRepository;
import com.example.ErrorLogAPI.repositories.ProjectRepository;
import com.example.ErrorLogAPI.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// represents the request routes for authentication
@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;

    // constructor for the authentication controller
    public AuthenticationController(UserRepository userRepository, ProjectRepository projectRepository,
                                    ErrorRepository errorRepository, PasswordRepository passwordRepository) {
        this.authenticationService = new AuthenticationService(userRepository, projectRepository, errorRepository, passwordRepository);
    }

    // represents a login route
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        /*
        LoginRequest loginRequest -> represents the login data
         */
        return this.authenticationService.login(loginRequest);
    }

    // represents a logout route
    @PostMapping("/auth/logout")
    public void logout() {

    }

        // represents a register route
        @PostMapping("/auth/register")
        public ResponseEntity<User> register(@RequestBody User u) {
        /*
        User u -> represents the user to register
         */
            return this.authenticationService.register(u);
    }
}
