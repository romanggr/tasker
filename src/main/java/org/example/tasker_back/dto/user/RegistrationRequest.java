package org.example.tasker_back.dto.user;

import lombok.Data;
import org.example.tasker_back.enums.Role;

import java.util.List;

@Data
public class RegistrationRequest {
    private String fullName;
    private String password;
    private String email;
    private List<Role> roles;
}
