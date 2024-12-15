package org.example.tasker_back.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdatePasswordRequest {
    private String id;
    private String newPassword;
    private String currentPassword;
}
