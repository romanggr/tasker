package org.example.tasker_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.DeleteTeamRequest;
import org.example.tasker_back.dto.team.LeaveTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
import org.example.tasker_back.model.Team;
import org.example.tasker_back.service.team.TeamServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamServiceImpl teamService;


    @GetMapping("/{email}")
    public ResponseEntity<List<Team>> getUserTeams(@PathVariable String email) {
        List<Team> response = teamService.getUserTeams(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestBody CreateTeamRequest request) {
        Team response = teamService.createTeam(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Team> updateTeam(@RequestBody UpdateTeamRequest request) {
        Team response = teamService.updateTeam(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTeam(@RequestBody DeleteTeamRequest request) {
        teamService.deleteTeam(request);
        return ResponseEntity.ok("Team deleted successfully");
    }

    @PutMapping("/leave")
    public ResponseEntity<String> leaveTeam(@RequestBody LeaveTeamRequest request) {
        teamService.leaveTeam(request);
        return ResponseEntity.ok("Team left successfully");
    }
}
