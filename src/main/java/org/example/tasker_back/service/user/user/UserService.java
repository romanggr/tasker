package org.example.tasker_back.service.user.user;

import org.example.tasker_back.dto.user.AuthResponse;
import org.example.tasker_back.dto.user.UpdatePasswordRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;

public interface UserService {
    AuthResponse updateUser(UpdateUserRequest request);

    AuthResponse updatePassword(UpdatePasswordRequest request);

}
