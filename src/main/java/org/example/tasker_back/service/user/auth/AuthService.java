package org.example.tasker_back.service.user.auth;


import org.example.tasker_back.dto.user.*;

public interface AuthService {
    AuthResponse register(RegistrationRequest request);

    AuthResponse login(LoginRequest request);

}
