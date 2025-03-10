package org.example.tasker_back.dto.team;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateTeamRequest {
    private String name;
    private List<String> collaboratorsEmails;
    private String creatorEmail;
}
