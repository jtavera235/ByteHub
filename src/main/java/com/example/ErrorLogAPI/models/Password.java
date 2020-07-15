package com.example.ErrorLogAPI.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// represents the passwoord database
@Document(collection = "Passwords")
public class Password {

    @Id
    private String id;
    // represents the user's email
    private String email;
    // represents the list of passwords stored with the user -> the honeyserver
    private List<String> password = new ArrayList<>();

    // default constructor for a password
    public Password() { super(); }

    // constructor for a password
    public Password(String email, String pass) {
        this.email = email;
        this.password = generateList(pass);
    }

    // returns the email of the user
    public String getEmail() { return this.email; }

    // returns the list of passwords for a user
    public List<String> getPassword() { return this.password; }

    // generates a list of random passwords stored with the user password
    public List<String> generateList(String password) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}|;:,<.>/?";
        for (int i = 0; i < 6; i++) {
            String pass = "";
            for (int h = 0; h < 8; h++) {
                Random rand = new Random();
                int index = rand.nextInt(characters.length());
                String temp = characters.substring(index, index + 1);
                pass += temp;
            }
            if (!pass.equals(password)) {
                this.password.add(pass);
            } else {
                i--;
            }
        }
        return this.password;
    }

    // adds the user password to the list of randomly generated passwords at a random index and
    // returns the index to save in the user database
    public int addPasswordRandomly(String pw) {
        Random rand = new Random();
        int passwordSize = this.password.size();
        int randomIndex = rand.nextInt(passwordSize);
        this.password.add(randomIndex, pw);
        return randomIndex;
    }

    // determine if the passed password exist in the list of passwords
    public boolean passwordExists(String password) {
        return this.password.contains(password);
    }

    // determine if the passed password is one of the fake passwords
    public boolean isCorrectPassword(int index, String password) {
        return this.password.get(index).equals(password);
    }

    // determine if the user's account is potentially being attacked
    public boolean isAccountBeingAttacked(int index, String password) {
        if (passwordExists(password) && !isCorrectPassword(index, password)) {
            return true;
        }
        return false;
    }
}
