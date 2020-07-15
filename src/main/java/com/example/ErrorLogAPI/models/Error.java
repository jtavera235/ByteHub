package com.example.ErrorLogAPI.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

public class Error {


    private int id;
    private String message;
    private int statusCode;
    private Date date;
    private String identifier;

    // default constructor for an error
    public Error() {
        super();
    }

    // constructor for an error
    public Error(String message, int statusCode, String identifier) {
        this.message = message;
        this.statusCode = statusCode;
        this.date = new Date();
        this.identifier = identifier;
    }

    // gets an error's message
    public String getMessage() {
        return this.message;
    }

    // gets an error's status code
    public int getStatusCode() {
        return this.statusCode;
    }

    // gets an error's date
    public Date getDate() {
        return this.date;
    }

    // gets an error's identifier
    public String getIdentifier() {
        return this.identifier;
    }


    // gets the ID of the error
    public int getId() { return this.id; }

    // sets an error message
    public void setMessage(String message) {
        this.message = message;
    }

    // sets an error status code
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    // sets an error identifier
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    // sets the date of the error
    public void setDate() {
        this.date = new Date();
    }

    // sets thee error id
    public void setId(int id) {
        this.id = id;
    }

}
