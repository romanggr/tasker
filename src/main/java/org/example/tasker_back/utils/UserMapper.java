package org.example.tasker_back.utils;

import org.example.tasker_back.dto.user.RegistrationRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;
import org.example.tasker_back.dto.user.UserDTO;
import org.example.tasker_back.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserMapper {
    public static User createUser(RegistrationRequest request) {
        if (request == null) throw new IllegalArgumentException("Request is null or empty");
        if (request.getEmail() == null || request.getEmail().isEmpty())
            throw new IllegalArgumentException("Email is null or empty");
        if (request.getPassword() == null || request.getPassword().isEmpty())
            throw new IllegalArgumentException("Password is null or empty");
        if (request.getFullName() == null || request.getFullName().isEmpty())
            throw new IllegalArgumentException("Full name is null or empty");
        if (request.getRoles() == null || request.getRoles().isEmpty())
            throw new IllegalArgumentException("Roles is null or empty");


        User newUser = new User();
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        newUser.setEmail(request.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setFullName(request.getFullName());
        newUser.setRoles(request.getRoles());

        return newUser;
    }


    public static UserDTO toDTO(User user) {
        if (user == null) throw new IllegalArgumentException("User is null or empty");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        userDTO.setTaskIds(user.getTaskIds());
        userDTO.setTeamIds(user.getTeamIds());

        return userDTO;
    }

    public static User updateUser(UpdateUserRequest request, User user) {
        if (request == null) throw new IllegalArgumentException("Request is null or empty");

        user.setFullName(request.getFullName() == null || request.getFullName().isEmpty() ? user.getFullName() : request.getFullName());
        user.setEmail(request.getEmail() == null || request.getEmail().isEmpty() ? user.getEmail() : request.getEmail());
        user.setRoles(request.getRoles() == null || request.getRoles().isEmpty() ? user.getRoles() : request.getRoles());

        return user;
    }
}
