package org.example.tasker_back.dto.user;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {
    private String token;
    private UserDTO userDto;
}
