package com.example.ErrorLogAPI.authentication;

import com.example.ErrorLogAPI.models.Project;
import com.example.ErrorLogAPI.models.User;

// class which represents the response that is given when a user logs in
public class LoginResponse {

    private Project project;
    private User user;

    // constructor for a login response
    public LoginResponse(Project project, User user) {
        this.project = project;
        this.user = user;
    }

    // returns the response project
    public Project getProject() { return this.project; }

    // returns the response user
    public User getUser() { return this.user; }

    // sets the project
    public void setProject(Project project) { this.project = project; }

    // sets the user
    public void setUser(User user) { this.user = user; }
}
