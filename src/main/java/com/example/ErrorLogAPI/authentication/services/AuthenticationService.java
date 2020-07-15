package com.example.ErrorLogAPI.authentication.services;

import com.example.ErrorLogAPI.authentication.models.LoginRequest;
import com.example.ErrorLogAPI.authentication.models.LoginResponse;
import com.example.ErrorLogAPI.models.Password;
import com.example.ErrorLogAPI.models.Project;
import com.example.ErrorLogAPI.models.User;
import com.example.ErrorLogAPI.repositories.ErrorRepository;
import com.example.ErrorLogAPI.repositories.PasswordRepository;
import com.example.ErrorLogAPI.repositories.ProjectRepository;
import com.example.ErrorLogAPI.repositories.UserRepository;
import com.example.ErrorLogAPI.services.PasswordService;
import com.example.ErrorLogAPI.services.ProjectService;
import com.example.ErrorLogAPI.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class AuthenticationService {

    UserRepository userRepository;
    ProjectRepository projectRepository;
    ErrorRepository errorRepository;
    PasswordRepository passwordRepository;

    private UserService userService;
    private ProjectService projectService;
    private PasswordService passwordService;

    // constructor for the authentication service
    public AuthenticationService(UserRepository userRepository, ProjectRepository projectRepository, ErrorRepository errorRepository,
                                 PasswordRepository passwordRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.errorRepository = errorRepository;
        this.passwordRepository = passwordRepository;
    }

    // performs the register actions in the services
    public ResponseEntity<User> register(User u) {
        /*
        User u -> represents the user to register
         */
        this.userService = new UserService(this.userRepository, this.projectRepository, this.errorRepository, this.passwordRepository);
        return this.userService.addNewUser(u);
    }

    // performs the login actions in the services
     public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        /*
        LoginRequest loginRequest -> represents the login data
         */
         this.userService = new UserService(this.userRepository, this.projectRepository, this.errorRepository, this.passwordRepository);
         this.projectService = new ProjectService(this.projectRepository, this.userRepository, this.errorRepository, this.passwordRepository);
         this.passwordService = new PasswordService(this.passwordRepository);

         // 1. Determine if the passed URL is a valid project URL
         String projectURL = loginRequest.getProjectURL();
         Optional<Project> project = this.projectService.findProjectByUrl(projectURL);
         if (!project.isPresent()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

         // 2. Determines if the passed email is part of the members list
         String email = loginRequest.getEmail();
         if (!project.get().isUserMember(email)) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

         // 3. Finds the user in the database
         Optional<User> foundUser = this.userService.findByEmail(email);
         if (!foundUser.isPresent()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

         // 4. Determines if the passed password corresponds to the passed email
         String password = loginRequest.getPassword();
         int passwordIndex = foundUser.get().getPassword();
         Optional<Password> dbPassword = this.passwordService.findByEmail(email);
         if (!dbPassword.isPresent() || !dbPassword.get().isCorrectPassword(passwordIndex, password)) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         LoginResponse response = new LoginResponse(project.get(), foundUser.get());
         return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
     }
}
