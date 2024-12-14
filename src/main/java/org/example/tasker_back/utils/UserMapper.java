package org.example.tasker_back.utils;

import org.example.tasker_back.dto.user.RegistrationRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;
import org.example.tasker_back.dto.user.UserDTO;
import org.example.tasker_back.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserMapper {
    public static User createUser(RegistrationRequest request, BCrypt bCrypt) {
        if (request == null) {
            throw new IllegalArgumentException("Request is null or empty");
        }

        User newUser = new User();

        newUser.setEmail(request.getEmail());
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        newUser.setPassword(hashedPassword);
        newUser.setFullName(request.getFullName());
        newUser.setRoles(request.getRoles());

        return newUser;
    }

    public static UserDTO toDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null or empty");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

    public static User updateUser(UpdateUserRequest request, User user) {
        if (request == null) {
            throw new IllegalArgumentException("Request is null or empty");
        }

        user.setId(request.getId());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        return user;
    }
}
