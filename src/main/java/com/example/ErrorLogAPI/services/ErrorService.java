package com.example.ErrorLogAPI.services;

import com.example.ErrorLogAPI.models.Error;
import com.example.ErrorLogAPI.models.Errors;
import com.example.ErrorLogAPI.models.ProjectID;
import com.example.ErrorLogAPI.repositories.ErrorRepository;
import com.example.ErrorLogAPI.repositories.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Iterator;
import java.util.Optional;

public class ErrorService {

    ErrorRepository errorRepository;
    ProjectRepository projectRepository;

    // constructor for the error service
    public ErrorService(ErrorRepository errorRepository, ProjectRepository projectRepository) {
        this.errorRepository = errorRepository;
        this.projectRepository = projectRepository;
    }

    // create a new instance of a list of errors for a project
    public Errors createErrors(Errors e) {
        /*
        Errors e -> represents the errors object that maps to the new project
         */
        this.errorRepository.save(e);
        return e;
    }

    // adds a new error
    public ResponseEntity<Error> addError(Error error, String pid, String key) {
        /*
        Error error -> represents the error
        String pid -> represents the project ID
        String key -> represents the user request key
         */
        ProjectID projectID = new ProjectID(pid, key, this.projectRepository);
        if (projectID.doesIdAndKeyMatch()) {
            Errors errors = findErrorsByProjectId(projectID.getProjectId());
            int size = errors.getErrors().size();
            error.setDate();
            error.setId(size + 1);
            errors.addError(error);
            this.errorRepository.save(errors);
            return new ResponseEntity<>(error, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // gets an error from the list of errors
    public ResponseEntity<Errors> getErrors(String id) {
        /*
        String id -> represents the id of the errors object in the database
         */
        Optional<Errors> errors = this.errorRepository.findById(id);
        if (errors.isPresent()) {
            return new ResponseEntity<>(errors.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(errors.get(), HttpStatus.NOT_FOUND);
    }

    // deletes an error from the database
    public ResponseEntity<String> deleteError(int errorID, String projectId) {
        /*
        int errorID -> represents the id of a specific error
        String projectID -> represents the id of the project
         */
        Errors errors = findErrorsByProjectId(projectId);
        for (Iterator<Error> iterator = errors.getErrors().iterator(); iterator.hasNext();) {
            Error error = iterator.next();
            if (error.getId() == errorID) {
                iterator.remove();
                this.errorRepository.save(errors);
                return new ResponseEntity<>("Log deleted successfully.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Log deleted unsuccessfully.", HttpStatus.NOT_FOUND);
    }

    // deletes the errors instance once a project was deleted
    public ResponseEntity<String> deleteErrorsFromDatabase(String projectId) {
         /*
        String projectID -> represents the id of the project
         */
        Errors errors = findErrorsByProjectId(projectId);
        if (errors == null) { return new ResponseEntity<>("Errors deleted unsuccessfully.", HttpStatus.NOT_FOUND); }
        this.errorRepository.deleteById(errors.getId());
        return new ResponseEntity<>("Errors deleted successfully.", HttpStatus.OK);
    }

    // finds an error based on the project ID
    public Errors findErrorsByProjectId(String projectID) {
        return this.errorRepository.findByProjectId(projectID);
    }
}
