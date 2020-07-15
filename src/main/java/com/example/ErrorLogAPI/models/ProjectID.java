package com.example.ErrorLogAPI.models;

import com.example.ErrorLogAPI.repositories.ProjectRepository;

import java.util.List;

// class which maps the shortened project ID in urls and a full project's ID in the database to compare
// if they are equal. Used when a new error is being created
public class ProjectID {

    // represents the shortened project ID found in the URL
    private final String projectConcatId;
    // represents the user's URL key
    private final String userKey;
    ProjectRepository projectRepository;
    // represents a project database ID
    private String projectId;

    // constructor for a project ID
    public ProjectID(String projectConcatId, String userKey, ProjectRepository projectRepository) {
        this.projectConcatId = projectConcatId;
        this.userKey = userKey;
        this.projectRepository = projectRepository;
    }

    // gets the shortened project id
    public String getProjectConcatId() { return this.projectConcatId; }

    // gets the user key
    public String getUserKey() { return this.userKey; }

    // gets the project id
    public String getProjectId() { return this.projectId; }

    // parses the passed user key and project id to verify that it is a project URL
    public boolean doesIdAndKeyMatch() {
        List<Project> projects = this.projectRepository.findAll();
        for (Project p : projects) {
            if (projectMatch(p)) {
                this.projectId = p.getId();
                return true;
            }
        }
        return false;
    }

    // determines if the passed project matches the project ID object parameters
    public boolean projectMatch(Project p) {
        String projectIdConcat = p.getId().substring(0, 5) + p.getId().substring(p.getId().length() - 4);
        String projectUserKey = p.getUserRequestKey();
        return projectIdConcat.equals(this.projectConcatId) && projectUserKey.equals(this.userKey);
    }
}
