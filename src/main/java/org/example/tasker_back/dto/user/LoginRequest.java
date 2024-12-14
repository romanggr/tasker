package org.example.tasker_back.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
