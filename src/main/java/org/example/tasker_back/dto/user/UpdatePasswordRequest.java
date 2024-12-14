package org.example.tasker_back.dto.user;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String id;
    private String newPassword;
    private String currentPassword;
}
