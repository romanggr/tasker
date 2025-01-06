package org.example.tasker_back.dto.team;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LeaveTeamRequest {
    private String userEmail;
    private String teamId;
}
