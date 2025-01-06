package org.example.tasker_back.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.tasker_back.enums.Role;

import java.util.List;

@AllArgsConstructor
@Data
public class UpdateUserRequest {
    private String id;
    private String email;
    private String fullName;
    private List<Role> roles;
}
