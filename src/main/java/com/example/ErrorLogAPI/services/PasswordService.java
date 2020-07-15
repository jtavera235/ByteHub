package com.example.ErrorLogAPI.services;

import com.example.ErrorLogAPI.models.Password;
import com.example.ErrorLogAPI.repositories.PasswordRepository;

import java.util.Optional;

public class PasswordService {

    private final PasswordRepository passwordRepository;

    // constructor for a PasswordService
    public PasswordService(PasswordRepository passwordRepository) { this.passwordRepository = passwordRepository; }

    // user created an account and password gets saved in the honeyserver
    public int savePassword(String email, String pw) {
        Password password = new Password(email, pw);
        int index = password.addPasswordRandomly(pw);
        this.passwordRepository.save(password);
        return index;
    }

    // deletes a password entry for a user when they delete their accounts
    public void deletePasswordsWhenUserDeletesAccount(String email) {
        this.passwordRepository.deleteByEmail(email);
    }

    // finds a password using the passed email as a parameter
    public Optional<Password> findByEmail(String email) {
        return this.passwordRepository.findByEmail(email);
    }

}
