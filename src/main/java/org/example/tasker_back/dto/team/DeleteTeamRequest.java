package org.example.tasker_back.dto.team;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeleteTeamRequest {
    private String teamId;
    private String userEmail;
}
