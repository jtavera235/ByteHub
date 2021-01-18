package com.example.ErrorLogAPI.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Users")
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private int password;
    // represents a list of the project ID's a user is in
    private List<String> projects;
    private Date createdDate;
    private List<String> invitedProjects;
    private String tempPassHolder;
    private String token;


    // default constructor for users
    public User() {
        super();
    }

    // constructor for a user
    public User(String name, String email, List<String> projects, List<String> invitedProjects,
        String tempPassHolder, String token) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.projects = projects;
        this.createdDate = new Date();
        this.invitedProjects = invitedProjects;
        this.tempPassHolder = tempPassHolder;
        this.token = token;
    }

    // constructor for a user that does not show password
    public User(String name, String email, List<String> projects, List<String> invitedProjects,
        int password, String token) {
        this.name = name;
        this.email = email;
        this.projects = projects;
        this.createdDate = new Date();
        this.invitedProjects = invitedProjects;
        this.password = password;
        this.token = token;
    }

    // gets a user's email
    public String getEmail() {
        return this.email;
    }

    // returns a user's name
    public String getName() {
        return this.name;
    }

    // gets a user's password
    public int getPassword() {
        return this.password;
    }


    // gets a user's created date
    public Date getCreatedDate() { return this.createdDate; }

    // gets the list of project's id that a user still has to accept
    public List<String> getInvitedProjects() {
        return this.invitedProjects;
    }

    // gets a user's ID
    public String getId() {
        return this.id;
    }

    // gets a user's project ID
    public List<String> getProjects() {
        return this.projects;
    }

    // gets the temporary password holder of the user
    public String getTempPassHolder() { return this.tempPassHolder; }

    // gets a user's token
    public String getToken() { return this.token; }

    // sets a user's email
    public void setEmail(String email) {
        this.email = email;
    }

    // sets a user's name
    public void setName(String name) {
        this.name = name;
    }

    // sets a user's password
    public void setPassword(int password) {
        this.password = password;
    }

    // sets a user's ID
    public void setId(String id) {
        this.id = id;
    }

    // sets a user's projects
    public void setProjects(ArrayList<String> projects) {
        this.projects = projects;
    }

    // sets the date that a user created the account
    public void setCreatedDate(Date d) {
        this.createdDate = d;
    }

    // sets a user's token
    public void setToken(String token) {
        this.token = token;
    }

    // adds a project to a user's list of project
    public void addProject(String projectID) {
        this.projects.add(projectID);
    }

    // remove a project from a user's project
    public void removeProject(String projectID) {
        for (int i = 0; i < this.projects.size(); i++) {
            if (this.projects.get(i).equals(projectID)) {
                this.projects.remove(i);
            }
        }
    }


    // adds a new project to a list of invited projects
    public void addNewInvitedProject(String projectID) {
        this.invitedProjects.add(projectID);
    }

    // delete a project from a list of invited projecta
    public void deleteInvitedProject(String projectID) {
        for (int i = 0; i < this.invitedProjects.size(); i++) {
            if (this.invitedProjects.get(i).equals(projectID)) {
                this.invitedProjects.remove(i);
            }
        }
    }

    // deletes a project from the invited list and adds it to the new project list
    public void acceptedProject(String projectID) {
        this.deleteInvitedProject(projectID);
        this.addProject(projectID);
    }

}



