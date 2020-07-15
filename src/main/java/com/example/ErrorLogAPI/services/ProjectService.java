package com.example.ErrorLogAPI.services;

import com.example.ErrorLogAPI.models.Error;
import com.example.ErrorLogAPI.models.Errors;
import com.example.ErrorLogAPI.models.Project;
import com.example.ErrorLogAPI.repositories.ErrorRepository;
import com.example.ErrorLogAPI.repositories.PasswordRepository;
import com.example.ErrorLogAPI.repositories.ProjectRepository;
import com.example.ErrorLogAPI.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ErrorRepository errorRepository;
    private final PasswordRepository passwordRepository;
    UserService userService;
    ErrorService errorService;

    // constructor for project service
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, ErrorRepository errorRepository,
                          PasswordRepository passwordRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.errorRepository = errorRepository;
        this.passwordRepository = passwordRepository;
    }

    // gets a project based on the ID
    public Optional<Project> getProject(String id) {
        /*
        String id -> represents the project ID
         */
        return this.projectRepository.findById(id);
    }

    // creates a new project
    public ResponseEntity<Project> addNewProject(Project project, String id) {
        /*
        Project project -> Represents a project object to add to the database
        String id -> Represents a user ID to save the new project in
         */
        // TODO -> Figure out how to not save to DB twice
        this.userService = new UserService(this.userRepository, this.projectRepository, this.errorRepository, this.passwordRepository);
        this.errorService = new ErrorService(this.errorRepository, this.projectRepository);
        // 1. gets the email of the passed user ID
        String email = this.userService.mapIdToEmail(id);

        // 2. Sets project properties based on found user
        project.setCreatorUserID(id);
        project.addMember(email);
        project.setCreatedDate(new Date());
        project.addAdmin(email);

        // 3. Saves the project in the database
        project = this.projectRepository.save(project);

        // 4. Sets the project URL using the id that was created when the project was
        //    previously saved and saves it again
        String projectId = project.getId();
        project.setURL(project.generateRequestURL());
        this.projectRepository.save(project);

        // 5. Adds the project to the user's list of projects
        this.userService.createdProject(id, project.getId());

        // 6. Initializes an empty error object in the database that maps to the created project
        Errors e = new Errors(project.getId(), new ArrayList<Error>());
        this.errorService.createErrors(e);

        // 7. Saves the project as a new project object with the creatorID as null. This is sent back to the client
        project = new Project(project.getName(), project.getNotifyError(), project.getURL(), project.getMembers(),
                project.getUserRequestKey(), project.getProjectAccountType(), project.getAdmins());
        project.setId(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    // updates a project's settings
    public Project updateProject(Project p, String id) {
        /*
        Project p -> represents the new project object to save in the database
        String id -> represents the project ID
         */
        return this.projectRepository.findById(id).map(project -> {
            project.setName(p.getName());
            project.setNotifyError(p.getNotifyError());
            project.setAdmins(p.getAdmins());
            project.setProjectAccountType(p.getProjectAccountType());
            return this.projectRepository.save(project);
        }).orElseGet(() -> {
            p.setId(id);
            return this.projectRepository.save(p);
        });
    }

    // deletes a project from the database
    public ResponseEntity<String> deleteProject(String id) {
        /*
        String id -> represents the id of the project that will be deleted
         */
        this.userService = new UserService(this.userRepository, this.projectRepository, this.errorRepository, this.passwordRepository);
        this.errorService = new ErrorService(this.errorRepository, this.projectRepository);
        this.userService.deleteProject(id);
        this.projectRepository.deleteById(id);
        this.errorService.deleteErrorsFromDatabase(id);
        return new ResponseEntity<>("Project deleted successfully.", HttpStatus.OK);
    }

    // user accepted project invite
    public Optional<Project> addMember(String projectID, String memberEmail) {
        /*
        String projectID -> represents the project ID
        String memberEmail -> represents the email of the user
         */
        this.userService = new UserService(this.userRepository, this.projectRepository, this.errorRepository, this.passwordRepository);
        return this.projectRepository.findById(projectID).map(project -> {
            project.addMember(memberEmail);
            return this.projectRepository.save(project);
        });
    }

    // invites a user to become a member of the project
    public ResponseEntity<String> inviteMember(String projectID, String email) {
        /*
        String projectID -> represents the id of the project
        String email -> represents the email of the invited user
         */
        this.userService = new UserService(this.userRepository, this.projectRepository, this.errorRepository, this.passwordRepository);
        this.userService.invitedUser(email, projectID);
        return new ResponseEntity<>("Member invited successfully.", HttpStatus.OK);
    }

    // removes a member from the project
    public ResponseEntity<String> deleteMember(String projectId, String email) {
        /*
       String projectID -> Represents the project ID
       String email -> Represents the email of the member that will be deleted
         */
        this.userService = new UserService(this.userRepository, this.projectRepository, this.errorRepository, this.passwordRepository);
        this.projectRepository.findById(projectId).map(project -> {
            project.deleteMember(email);
            this.userService.removeFromProject(email, projectId);
            this.projectRepository.save(project);
            return new ResponseEntity<>("Member deleted successfully.", HttpStatus.OK);
        });
        return new ResponseEntity<>("Member deleted successfully.", HttpStatus.NOT_FOUND);
    }

    // adds an admin to a project
    public ResponseEntity<String> addAdmin(String projectId, String email) {
        /*
        String projectID -> Represents the project id
        String email -> Represents the email of the user
         */
        this.projectRepository.findById(projectId).map(project -> {
            project.addAdmin(email);
            this.projectRepository.save(project);
            return new ResponseEntity<>("Admin added successfully.", HttpStatus.OK);
        });
        return new ResponseEntity<>("Admin added successfully.", HttpStatus.NOT_FOUND);
    }

    // removes an admin from a project
    public ResponseEntity<String> removeAdmin(String projectId, String email) {
         /*
        String projectID -> Represents the project id
        String email -> Represents the email of the user
         */
        this.projectRepository.findById(projectId).map(project -> {
            project.removeAdmin(email);
            this.projectRepository.save(project);
            return new ResponseEntity<>("Admin deleted successfully.", HttpStatus.OK);
        });
        return new ResponseEntity<>("Admin deleted unsuccessfully.", HttpStatus.NOT_FOUND);
    }

    // searches the database for a project based on the projectURL
    public Optional<Project> findProjectByUrl(String url) {
        return this.projectRepository.findByURL(url);
    }
}
