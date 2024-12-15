package org.example.tasker_back.service.team;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.DeleteTeamRequest;
import org.example.tasker_back.dto.team.LeaveTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
import org.example.tasker_back.exceptions.EntityNotFoundException;
import org.example.tasker_back.model.Task;
import org.example.tasker_back.model.Team;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.TaskRepository;
import org.example.tasker_back.repository.TeamRepository;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.utils.TeamMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<Team> getUserTeams(String userEmail) {
        return teamRepository.findTeamByCollaboratorsEmails(userEmail);
    }

    @Override
    public Team createTeam(CreateTeamRequest request) {
        Team team = TeamMapper.toEntityCreate(request);
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(UpdateTeamRequest request) {
        Team dbTeam = teamRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        if (!request.getCreatorEmail().equals(dbTeam.getCreatorEmail())) {
            throw new IllegalArgumentException("Only creator can update team");
        }

        Team team = TeamMapper.toEntityUpdate(request);
        return teamRepository.save(team);
    }

    @Transactional
    @Override
    public void deleteTeam(DeleteTeamRequest request) {
        Team dbTeam = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found by this id"));

        if (!request.getUserEmail().equals(dbTeam.getCreatorEmail())) {
            throw new IllegalArgumentException("Only creator can delete team");
        }

        // Delete all tasks associated with the team
        List<Task> tasks = dbTeam.getTasks();
        if (tasks != null && !tasks.isEmpty()) {
            taskRepository.deleteAll(tasks);
        }

        List<String> collaboratorEmails = dbTeam.getCollaboratorsEmails();
        List<User> collaborators = userRepository.findByEmailIn(collaboratorEmails);

        // Remove the team from collaborators' teamIds
        for (User user : collaborators) {
            user.setTeamIds(user.getTeamIds().stream()
                    .filter(teamId -> !teamId.equals(dbTeam.getId()))
                    .collect(Collectors.toList()));
        }

        userRepository.saveAll(collaborators);
        teamRepository.delete(dbTeam);
    }



    @Override
    @Transactional
    public void leaveTeam(LeaveTeamRequest request) {
        Team dbTeam = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found by this id"));

        Team oldDbTeam = new Team(
                dbTeam.getId(),
                dbTeam.getName(),
                new ArrayList<>(dbTeam.getTasks()),
                new ArrayList<>(dbTeam.getCollaboratorsEmails()),
                dbTeam.getCreatorEmail()
        );

        // Handle the case when the creator is leaving
        if (request.getUserEmail().equals(dbTeam.getCreatorEmail())) {
            if (dbTeam.getCollaboratorsEmails() != null && dbTeam.getCollaboratorsEmails().size() == 1) {
                teamRepository.delete(dbTeam);
            } else {
                String newCreatorEmail = dbTeam.getCollaboratorsEmails().get(0);
                dbTeam.setCreatorEmail(newCreatorEmail);

                List<String> newCollaborators = dbTeam.getCollaboratorsEmails().stream()
                        .filter(email -> !email.equals(request.getUserEmail()))
                        .collect(Collectors.toList());
                dbTeam.setCollaboratorsEmails(newCollaborators);
            }
        } else {
            // Handle the case when a collaborator (not the creator) is leaving
            dbTeam.setCollaboratorsEmails(dbTeam.getCollaboratorsEmails().stream()
                    .filter(email -> !email.equals(request.getUserEmail()))
                    .collect(Collectors.toList()));
        }

        List<String> collaboratorEmails = oldDbTeam.getCollaboratorsEmails();
        List<User> collaborators = userRepository.findByEmailIn(collaboratorEmails);

        for (User user : collaborators) {
            user.setTeamIds(user.getTeamIds().stream()
                    .filter(teamId -> !teamId.equals(oldDbTeam.getId()))
                    .collect(Collectors.toList()));
        }

        userRepository.saveAll(collaborators);
        teamRepository.save(dbTeam);
    }



}

