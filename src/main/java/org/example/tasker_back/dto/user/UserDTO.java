package org.example.tasker_back.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tasker_back.enums.Role;

import java.util.List;

@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String email;
    private String fullName;
    private List<Role> roles;
    private List<String> taskIds;
    private List<String> teamIds;
}
