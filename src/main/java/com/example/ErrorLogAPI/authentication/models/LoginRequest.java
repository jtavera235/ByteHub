package com.example.ErrorLogAPI.authentication.models;

// a class which represents the object that will be sent in a login request
public class LoginRequest {

    private String projectURL;
    private String email;
    private String password;

    // default constructor for LoginRequest
    public LoginRequest() { super(); }

    // constructor for LoginRequest
    public LoginRequest(String projectURL, String email, String password) {
        this.projectURL = projectURL;
        this.email = email;
        this.password = password;
    }

    // gets the project URL
    public String getProjectURL() { return this.projectURL; }

    // gets the request email
    public String getEmail() { return this.email; }

    // gets the request password
    public String getPassword() { return this.password; }

    // sets a project URL
    public void setProjectURL(String projectURL) { this.projectURL = projectURL; }

    // sets a project email
    public void setEmail(String email) { this.email = email; }

    // sets a project password
    public void setPassword(String password) { this.password = password; }
}
