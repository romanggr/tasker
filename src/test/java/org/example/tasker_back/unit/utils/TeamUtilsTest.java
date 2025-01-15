package org.example.tasker_back.unit.utils;

import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.utils.TeamUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TeamUtilsTest {
    @InjectMocks
    private TeamUtils teamUtils;

    @Test
    void validateCreateTeam_success() {
        CreateTeamRequest request = new CreateTeamRequest("Tolik", List.of("email1@gmail.com", "email2@gmail.com"), "creator@gmail.com");

        teamUtils.validateCreateTeam(request);
    }

    @Test
    void validateCreateTeam_invalidName() {
        CreateTeamRequest request = new CreateTeamRequest("", List.of("email1@gmail.com", "email2@gmail.com"), "creator@gmail.com");

        assertThrows(IllegalArgumentException.class, () -> teamUtils.validateCreateTeam(request));
    }

    @Test
    void validateCreateTeam_invalidCreator() {
        CreateTeamRequest request = new CreateTeamRequest("Tolik", List.of("email1@gmail.com", "email2@gmail.com"), "");

        assertThrows(IllegalArgumentException.class, () -> teamUtils.validateCreateTeam(request));
    }

    @Test
    void validateCreateTeam_invalidCreator_email() {
        CreateTeamRequest request = new CreateTeamRequest("Tolik", List.of("email1@gmail.com", "email2@gmail.com"), "tolikemail");

        assertThrows(IllegalArgumentException.class, () -> teamUtils.validateCreateTeam(request));
    }
}