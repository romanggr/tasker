package org.example.tasker_back.dto.team;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTeamRequest {
    private String id;
    private String name;
    private List<String> collaboratorsEmails;
    private String creatorEmail;
}
