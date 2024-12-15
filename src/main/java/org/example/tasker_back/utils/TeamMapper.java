package org.example.tasker_back.utils;

import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
import org.example.tasker_back.model.Team;

import java.util.List;

public class TeamMapper {

    public static Team toEntityCreate(CreateTeamRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create request = null");
        };

        Team entity = new Team();
        entity.setName(request.getName());
        entity.setCollaboratorsEmails(request.getCollaboratorsEmails());
        entity.setTasks(List.of());
        entity.setCreatorEmail(request.getCreatorEmail());

        return entity;
    }

    public static Team toEntityUpdate(UpdateTeamRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create request = null");
        };

        Team entity = new Team();
        entity.setName(request.getName());
        entity.setCollaboratorsEmails(request.getCollaboratorsEmails());
        entity.setTasks(List.of());
        entity.setCreatorEmail(request.getCreatorEmail());

        return entity;
    }
}
