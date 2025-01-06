package org.example.tasker_back.utils;

import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
import org.example.tasker_back.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamMapper {

    public static Team toEntityCreate(CreateTeamRequest request, List<String> collaborators) {
        Team entity = new Team();

        if (request == null) {
            throw new IllegalArgumentException("Create request = null");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (request.getCreatorEmail() == null || request.getCreatorEmail().isEmpty()) {
            throw new IllegalArgumentException("Creator email cannot be null or empty");
        }

        entity.setName(request.getName());
        entity.setCollaboratorsEmails(collaborators);
        entity.setTasks(new ArrayList<>());
        entity.setCreatorEmail(request.getCreatorEmail());

        return entity;
    }

    public static Team toEntityUpdate(UpdateTeamRequest request, Team entity) {
        if (request == null) {
            throw new IllegalArgumentException("Create request = null");
        }

        entity.setName(request.getName());
        entity.setCollaboratorsEmails(request.getCollaboratorsEmails());

        return entity;
    }
}
