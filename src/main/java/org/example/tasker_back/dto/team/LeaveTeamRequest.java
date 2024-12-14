package org.example.tasker_back.dto.team;

import lombok.Data;

@Data
public class LeaveTeamRequest {
    private String userEmail;
    private String teamId;
}
