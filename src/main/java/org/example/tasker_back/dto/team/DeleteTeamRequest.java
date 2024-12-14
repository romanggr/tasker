package org.example.tasker_back.dto.team;

import lombok.Data;

@Data
public class DeleteTeamRequest {
    private String userEmail;
    private String teamId;
}
