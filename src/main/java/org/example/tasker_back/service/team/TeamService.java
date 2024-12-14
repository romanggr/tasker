package org.example.tasker_back.service.team;

import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.DeleteTeamRequest;
import org.example.tasker_back.dto.team.LeaveTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
import org.example.tasker_back.model.Team;

import java.util.List;

public interface TeamService {
    List<Team> getUserTeams(String userEmail);

    Team createTeam(CreateTeamRequest request);

    Team updateTeam(UpdateTeamRequest team); // can do only creator

    void deleteTeam(DeleteTeamRequest request); // can do only creator

    void leaveTeam(LeaveTeamRequest request);

}
