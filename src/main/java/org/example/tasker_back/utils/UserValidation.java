package org.example.tasker_back.utils;

import org.example.tasker_back.dto.user.RegistrationRequest;
import org.example.tasker_back.dto.user.UpdatePasswordRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class UserValidation {

    public static void isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    public static void isEmailInUse(String newEmail, String currentEmail, UserRepository userRepository) {
        if (newEmail.equals(currentEmail)) {
            return;
        }

        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

    public static void isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }


        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
    }

    public static void isValidName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public static void isValidRegistration(RegistrationRequest request, UserRepository userRepository) {
        isValidEmail(request.getEmail());
        isValidPassword(request.getPassword());
        isValidName(request.getFullName());
        isEmailInUse(request.getEmail(), null, userRepository);
    }

    public static void isValidUpdatingUser(UpdateUserRequest request, String currentEmail, UserRepository userRepository) {
        isValidEmail(request.getEmail());
        isValidName(request.getFullName());
        isEmailInUse(request.getEmail(), currentEmail, userRepository);
    }

    public static void isValidUpdatingPassword(UpdatePasswordRequest request, String currentHashedPassword) {
        isValidPassword(request.getNewPassword());

        System.out.println(request.getCurrentPassword() + " " + currentHashedPassword);
        if (!BCrypt.checkpw(request.getCurrentPassword(), currentHashedPassword)) {
            throw new IllegalArgumentException("Please provide valid password");
        }
    }
}
