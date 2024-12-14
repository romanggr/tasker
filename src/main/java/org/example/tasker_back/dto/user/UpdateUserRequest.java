package org.example.tasker_back.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String id;
    private String email;
    private String fullName;
}
