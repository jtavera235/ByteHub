package com.example.ErrorLogAPI.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "Projects")
public class Project {

    @Id
    private String id;
    private String name;
    private String creatorUserID;
    private Date createdDate;
    private boolean notifyError;
    private String URL;
    private List<String> members;
    private String userRequestKey;
    private String projectAccountType;
    private List<String> admins;


    // default constructor for a Project
    public Project() {
        super();
    }

    // constructor for a project
    public Project(String name, boolean notifyError, String URL, List<String> members,
                   String userRequestKey, String projectAccountType, List<String> admins) {
        this.name = name;
        this.notifyError = notifyError;
        this.URL = URL;
        this.createdDate = new Date();
        this.members = members;
        this.userRequestKey = userRequestKey;
        this.projectAccountType = parseProjectAccountType(projectAccountType);
        this.admins = admins;
    }


    // Gets a project name
    public String getName() {
        return this.name;
    }

    // gets a project's id
    public String getId() { return this.id; }

    // gets a projects userID
    public String getCreatorUserID() {
        return this.creatorUserID;
    }

    // gets a project creation date
    public Date getCreationDate() {
        return this.createdDate;
    }

    // gets a project notifiy error option
    public boolean getNotifyError() {
        return this.notifyError;
    }

    // gets a project's URL
    public String getURL() {
        return this.URL;
    }

    // gets a project's team members
    public List<String> getMembers() { return this.members; }

    // gets the user generated id
    public String getUserRequestKey() { return this.userRequestKey; }

    // gets a project's list of admins
    public List<String> getAdmins() { return this.admins; }

    // gets a projects account type
    public String getProjectAccountType() { return this.projectAccountType; }

    // Sets a project name
    public void setName(String name) {
        this.name = name;
    }

    // sets a projects userID
    public void setCreatorUserID(String creatorUserID) {
        this.creatorUserID = creatorUserID;
    }

    // sets a project notify error option
    public void setNotifyError(boolean notifyError) {
        this.notifyError = notifyError;
    }

    // sets a project's URL
    public void setURL(String URL) {
        this.URL = URL;
    }

    // sets a project's ID
    public void setId(String id) { this.id = id; }

    // sets a project's admin
    public void setAdmins(List<String> admins) { this.admins = admins; }

    // sets a projects account type
    public void setProjectAccountType(String projectAccountType) { this.projectAccountType = projectAccountType; }


    // sets a project creation date
    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    // adds a new team member to the project
    public void addMember(String userId) {
        this.members.add(userId);
    }

    // adds a new admin to the list of admins
    public void addAdmin(String email) { this.admins.add(email); }

    // removes a user from an admin role
    public void removeAdmin(String email) {
        for (int i = 0; i < this.admins.size(); i++) {
            if (this.admins.get(i).equals(email)) {
                this.admins.remove(i);
            }
        }
    }

    // delete a team member from the list of members
    public void deleteMember(String email) {
        for (int i = 0; i < this.members.size(); i++) {
            if (this.members.get(i).equals(email)) {
                this.members.remove(i);
            }
        }
    }
    
    // generates a project's url
    public String generateRequestURL() {
        String projectIdShort = this.getId().substring(0, 5) + this.getId().substring(this.getId().length() - 4);
        return projectIdShort + "/" + this.getUserRequestKey();
    }

    // parses the passed project account type
    public String parseProjectAccountType(String accountType) {
        if (accountType.equals("FREE") || accountType.equals("MEMBER") || accountType.equals("PREMIUM")) {
            return accountType;
        }
        return "";
    }

    // determine if the passed user is a member
    public boolean isUserMember(String email) { return this.members.contains(email); }

    // returns the number of admins a project has
    public int numberOfAdmins() { return this.admins.size(); }
}
