package org.example.tasker_back.unit.utils;

import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
import org.example.tasker_back.model.Team;
import org.example.tasker_back.utils.TeamMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeamMapperTest {

    @Test
    void toEntityCreate_success() {
        CreateTeamRequest request = new CreateTeamRequest("Main Backend",
                List.of("user1@email.com"), "user3@email.com");
        List<String> collaborators = List.of("user1@email.com", "user3@email.com");

        Team response = TeamMapper.toEntityCreate(request, collaborators);

        assertNotNull(response);
        assertEquals("Main Backend", response.getName());
        assertEquals(2, response.getCollaboratorsEmails().size());
        assertTrue(response.getCollaboratorsEmails().contains("user1@email.com"));
        assertTrue(response.getCollaboratorsEmails().contains("user3@email.com"));
        assertEquals("user3@email.com", response.getCreatorEmail());
    }

    @Test
    void toEntityCreate_null() {
        assertThrows(IllegalArgumentException.class, () -> TeamMapper.toEntityCreate(null, null));
    }

    @Test
    void toEntityCreate_name_null() {
        CreateTeamRequest request = new CreateTeamRequest("",
                List.of("user1@email.com"), "user3@email.com");
        List<String> collaborators = List.of("user1@email.com", "user3@email.com");

        assertThrows(IllegalArgumentException.class, () -> TeamMapper.toEntityCreate(request, collaborators));
    }

    @Test
    void toEntityCreate_creatorEmail_null() {
        CreateTeamRequest request = new CreateTeamRequest("Main Backend",
                List.of("user1@email.com"), "");
        List<String> collaborators = List.of("user1@email.com", "user3@email.com");

        assertThrows(IllegalArgumentException.class, () -> TeamMapper.toEntityCreate(request, collaborators));
    }


    @Test
    void toEntityUpdate_success() {
        UpdateTeamRequest request = new UpdateTeamRequest("id123", "Main Backend",
                List.of("user1@email.com", "user3@email.com"), "user3@email.com");

        Team team = new Team("id123", "Backend", Collections.emptyList(), Collections.emptyList(), "user3@email.com");

        Team response = TeamMapper.toEntityUpdate(request, team);

        assertNotNull(request);
        assertEquals("id123", response.getId());
        assertEquals("Main Backend", response.getName());
        assertEquals(2, response.getCollaboratorsEmails().size());
        assertTrue(response.getCollaboratorsEmails().contains("user1@email.com"));
        assertTrue(response.getCollaboratorsEmails().contains("user3@email.com"));
        assertEquals("user3@email.com", response.getCreatorEmail());
    }

    @Test
    void toEntityUpdate_emptyName() {
        UpdateTeamRequest request = new UpdateTeamRequest("id123", "",
                List.of("user1@email.com", "user3@email.com"), "user3@email.com");

        Team team = new Team("id123", "Backend", Collections.emptyList(), Collections.emptyList(), "user3@email.com");

        Team response = TeamMapper.toEntityUpdate(request, team);

        assertNotNull(request);
        assertEquals("id123", response.getId());
        assertEquals("Backend", response.getName());
        assertEquals(2, response.getCollaboratorsEmails().size());
        assertTrue(response.getCollaboratorsEmails().contains("user1@email.com"));
        assertTrue(response.getCollaboratorsEmails().contains("user3@email.com"));
        assertEquals("user3@email.com", response.getCreatorEmail());
    }

    @Test
    void toEntityUpdate_null() {
        assertThrows(IllegalArgumentException.class, () -> TeamMapper.toEntityUpdate(null, new Team()));
    }

}
