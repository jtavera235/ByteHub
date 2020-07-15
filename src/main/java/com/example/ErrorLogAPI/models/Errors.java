package com.example.ErrorLogAPI.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Iterator;
import java.util.List;

@Document(collection = "Errors")
public class Errors {

    @Id
    private String id;
    private String projectId;
    private List<Error> errors;

    // default constructor for an Errors Object
    public Errors() { super(); }

    // constructor for Errors
    public Errors(String projectID, List<Error> errors) {
        this.projectId = projectID;
        this.errors = errors;
    }

    // gets the project ID
    public String getProjectID() { return this.projectId; }

    // gets the list of errors
    public List<Error> getErrors() { return this.errors; }

    // gets the errors ID
    public String getId() { return this.id; }

    // sets the project id
    public void setProjectID(String projectID) {
        this.projectId = projectID;
    }

    // adds an error to the list of errors
    public void addError(Error e) {
        this.errors.add(e);
    }

    // set errors
    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
