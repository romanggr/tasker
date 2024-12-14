package org.example.tasker_back.service.user;


import org.example.tasker_back.dto.user.*;

public interface AuthService {
    AuthResponse register(RegistrationRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse updateUser(UpdateUserRequest request);

    AuthResponse updatePassword(UpdatePasswordRequest request);
}
