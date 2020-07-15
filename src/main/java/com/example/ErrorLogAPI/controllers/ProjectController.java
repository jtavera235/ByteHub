package com.example.ErrorLogAPI.controllers;

import com.example.ErrorLogAPI.models.Project;
import com.example.ErrorLogAPI.repositories.ErrorRepository;
import com.example.ErrorLogAPI.repositories.PasswordRepository;
import com.example.ErrorLogAPI.repositories.ProjectRepository;
import com.example.ErrorLogAPI.repositories.UserRepository;
import com.example.ErrorLogAPI.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
public class ProjectController {

    ProjectService projectService;

    // constructor for a project controller
    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository,
                             ErrorRepository errorRepository, PasswordRepository passwordRepository) {
        this.projectService = new ProjectService(projectRepository, userRepository, errorRepository, passwordRepository);
    }

    // gets a project based on the passed id
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable String id) {
        /*
        String id -> The ID of the project
         */
        Optional<Project> project = this.projectService.getProject(id);
        if (project.isPresent()) {
            return new ResponseEntity<Project>(project.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Project>(HttpStatus.NOT_FOUND);
        }
    }

    // creates a project to the database
    @PostMapping("/projects/{id}")
    public ResponseEntity<Project> createProject(@RequestBody Project p, @PathVariable String id) {
        /*
        Project p -> The project being being saved to the database
        String id -> The ID of the user who created the project
         */
        return this.projectService.addNewProject(p, id);
    }

    // updates a project settings
    @PutMapping("/projects/{id}")
    public Project updateProject(@RequestBody Project p, @PathVariable String id) {
        /*
        Project p -> The project object with the updated values
        String id -> The ID of the project
         */
        return this.projectService.updateProject(p, id);
    }

    // deletes the project from the database and from a user's list of projects
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable String id) {
        /*
        String id -> The project id
         */
        return this.projectService.deleteProject(id);
    }

    // invites a member to join the project
    @PostMapping("/projects/{id}/members/{email}")
    public void inviteMembers(@PathVariable String id, @PathVariable String email) {
        /*
        String id -> The id of the project
        String email -> The email of the new member
         */
        this.projectService.inviteMember(id, email);
    }

    // removes a team member from the project
    @DeleteMapping("/projects/{id}/members/{email}")
    public ResponseEntity<String> deleteMember(@PathVariable String id, @PathVariable String email) {
        /*
        String id -> the id of the project
        String email -> the email of the member
         */
        return this.projectService.deleteMember(id, email);
    }

    // adds an admin to the project
    @PostMapping("/projects/{id}/admin/{email}")
    public ResponseEntity<String> addAdmin(@PathVariable String id, @PathVariable String email) {
        /*
        String id -> The id of the project
        String email -> The email of the user that will now become one of the admins
         */
        return this.projectService.addAdmin(id, email);
    }

    // removes an admin from the project
    @DeleteMapping("/projects/{id}/admin/{email}")
    public ResponseEntity<String> removeAdmin(@PathVariable String id, @PathVariable String email) {
        /*
        String id -> The id of the project
        String email -> The email of the user that will now become one of the admins
         */
        return this.projectService.removeAdmin(id, email);
    }
}