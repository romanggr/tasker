package org.example.tasker_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.service.team.TeamServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class TestController {
    private final TeamServiceImpl teamService;

    @PostMapping("/test")
    public String test() {
        List<String> collaborators = new ArrayList<>(List.of("email1@gmail.com", "email2@gmail.com"));
        CreateTeamRequest request = new CreateTeamRequest(null, collaborators, "creator@gmail.com");


        return teamService.createTeam(request).getId();
    }
}

// todo delete it