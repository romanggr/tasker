package org.example.tasker_back.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateUserRequest {
    private String id;
    private String email;
    private String fullName;
}
