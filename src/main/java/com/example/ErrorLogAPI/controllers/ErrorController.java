package com.example.ErrorLogAPI.controllers;

import com.example.ErrorLogAPI.models.Error;
import com.example.ErrorLogAPI.models.Errors;
import com.example.ErrorLogAPI.repositories.ErrorRepository;
import com.example.ErrorLogAPI.repositories.ProjectRepository;
import com.example.ErrorLogAPI.services.ErrorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ErrorController {

    ErrorService errorService;

    // constructor for error controller
    public ErrorController(ErrorRepository errorRepository, ProjectRepository projectRepository) {
        this.errorService = new ErrorService(errorRepository, projectRepository);
    }

    // gets the list of errors from the database
    @GetMapping("/errors/{id}")
    public ResponseEntity<Errors> getErrors(@PathVariable String id) {
        /*
        String id -> the id of the error in the DB
         */
        return this.errorService.getErrors(id);
    }

    // adds an error to the project and error database
    @PostMapping("/{pid}/{key}")
    public ResponseEntity<Error> addError(@RequestBody Error e, @PathVariable String pid, @PathVariable String key) {
        /*
        String pid -> the shortened project id this error belongs to
        String key -> the user inputted key
        Error e -> the error the user inputted
         */
        return this.errorService.addError(e, pid, key);
    }

    // delete an error from the database
    @DeleteMapping("/errors/{id}/projects/{projectID}")
    public ResponseEntity<String> deleteError(@PathVariable int id, @PathVariable String projectID) {
        /*
        int id -> the error ID
        String projectID -> The project ID
         */
        return this.errorService.deleteError(id, projectID);
    }

}
