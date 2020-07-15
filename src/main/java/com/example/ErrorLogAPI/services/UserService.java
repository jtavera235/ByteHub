package com.example.ErrorLogAPI.services;


import com.example.ErrorLogAPI.models.Password;
import com.example.ErrorLogAPI.models.Project;
import com.example.ErrorLogAPI.models.User;
import com.example.ErrorLogAPI.repositories.ErrorRepository;
import com.example.ErrorLogAPI.repositories.PasswordRepository;
import com.example.ErrorLogAPI.repositories.ProjectRepository;
import com.example.ErrorLogAPI.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ErrorRepository errorRepository;
    private final PasswordRepository passwordRepository;

    ProjectService projectService;
    PasswordService passwordService;

    // constructor for a user service
    public UserService(UserRepository userRepository, ProjectRepository projectRepository, ErrorRepository errorRepository,
    PasswordRepository passwordRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.errorRepository = errorRepository;
        this.passwordRepository = passwordRepository;
    }

    // gets all the users in the database
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    // Gets a specific user from the database
    public Optional<User> getUser(String id) {
        /*
        String id -> represents the id of the user
         */
        return this.userRepository.findById(id);
    }

    // Adds a user to the database
    public ResponseEntity<User> addNewUser(User u) {
        /*
        User u -> Represents the new user to add to the database
         */
        this.passwordService = new PasswordService(this.passwordRepository);
        int index = passwordService.savePassword(u.getEmail(), u.getTempPassHolder());
        u = new User(u.getName(), u.getEmail(), u.getProjects(), u.getInvitedProjects(), index);
        u.setCreatedDate(new Date());
        return new ResponseEntity<>(this.userRepository.save(u), HttpStatus.OK);
    }

    // updates a user
    public User updateUser(User u, String id) {
        /*
        User u -> Represents the updated user to add to the database
        String id -> represents the user's ID
         */
        return getUser(id).map(user -> {
            user.setName(u.getName());
            user.setEmail(u.getEmail());
            return this.userRepository.save(user);
        }).orElseGet(() -> {
            u.setId(id);
            return this.userRepository.save(u);
        });
    }

    // delete a User from the database
    public ResponseEntity<String> deleteUser(String id) {
         /*
        String id -> represents the user's ID
         */
        this.passwordService = new PasswordService(this.passwordRepository);
        this.projectService = new ProjectService(this.projectRepository, this.userRepository,
                this.errorRepository, this.passwordRepository);
        // 1. Finds the user based on the supplied ID
        Optional<User> user = getUser(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>("User not found.",
                    HttpStatus.NOT_FOUND);
        }

        // 2. Gets the list of project ID's a user is part of
        List<String> userProjects = user.get().getProjects();

        // 3. Go through each project ID and delete properties according to case
        for (int i = 0; i < userProjects.size(); i++) {
            Optional<Project> project = this.projectService.getProject(userProjects.get(i));
            if (project.isPresent()) {
                // CASE 1 : There is only 1 admin which has to be the passed user. Therefore delete the whole project
                if (project.get().numberOfAdmins() <= 1) {
                    this.projectService.deleteProject(userProjects.get(i));
                    this.passwordService.deletePasswordsWhenUserDeletesAccount(user.get().getEmail());
                    this.userRepository.deleteById(id);
                    return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
                }
                // CASE 2: There is more than 1 admin and therefore the user has to be removed from the admin list
                if (project.get().getAdmins().contains(user.get().getEmail())) {
                    project.get().removeAdmin(user.get().getEmail());
                }
                // 4. Remove the user from the list of members and save the project's new properties
                project.get().deleteMember(user.get().getEmail());
                this.projectRepository.save(project.get());
            }
        }
        // 5. Remove the user from the databse if haven't done so already and return response
        this.passwordService.deletePasswordsWhenUserDeletesAccount(user.get().getEmail());
        this.userRepository.deleteById(id);
        return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
    }

    // adds a project to a users list of project
    // called when a new project gets created
    public Optional<User> createdProject(String id, String projectID) {
         /*
        String projectID -> Represents the ID of the newly created project
        String id -> represents the user's ID
         */
        return getUser(id).map(user -> {
            user.addProject(projectID);
            return this.userRepository.save(user);
        });
    }

    // user accepted project invite
    public Optional<User> acceptedProject(String id, String projectID) {
         /*
        String projectID -> Represents the ID of the newly created project
        String id -> represents the user's ID
         */
        this.projectService = new ProjectService(this.projectRepository, this.userRepository, this.errorRepository, this.passwordRepository);
        return getUser(id).map(user -> {
            user.acceptedProject(projectID);
            String email = mapIdToEmail(id);
            this.projectService.addMember(projectID, email);
            return this.userRepository.save(user);
        });
    }

    // deletes a project from all the users who were part of the project
    public void deleteProject(String id) {
         /*
        String id -> Represents the ID of the newly created project
         */
        for (User u : this.userRepository.findAll()) {
            for (Iterator<String> iterator = u.getProjects().iterator(); iterator.hasNext();) {
                String val = iterator.next();
                if (val.equals(id)) {
                    iterator.remove();
                    this.userRepository.save(u);
                }
            }
        }
    }

    // user gets invited to the project
    public void invitedUser(String email, String projectID) {
         /*
        String projectID -> Represents the ID of the newly created project
        String email -> represents the invited user's email
         */
        String id = mapEmailToId(email);
        getUser(id).map(user -> {
            user.addNewInvitedProject(projectID);
            return this.userRepository.save(user);
        });
    }

    // removes a project from a user when they were removed from the project
    public void removeFromProject(String email, String projectId) {
          /*
        String projectID -> Represents the ID of the newly created project
        String email -> represents the user's email
         */
        String id = mapEmailToId(email);
        getUser(id).map(user -> {
            user.removeProject(projectId);
            return this.userRepository.save(user);
        });
    }

    // map's a user ID to its corresponding email
    public String mapIdToEmail(String userId) {
        Optional<String> userEmail = this.userRepository.findById(userId).map(user -> user.getEmail());
        if (userEmail.isPresent()) {
            return userEmail.get();
        } else {
            return null;
        }
    }

    // maps a user's email to its corresponding ID
    public String mapEmailToId(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getId();
        }
        return null;
    }

    // finds a user in the database through email
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = findByEmail(email);
        if (!user.isPresent()) { throw new UsernameNotFoundException("User not found by email: " + email); }
        return toUserDetails(user.get());
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getTempPassHolder())
                .roles(userObject.role).build();
    }
}
