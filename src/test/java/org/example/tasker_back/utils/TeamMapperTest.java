package org.example.tasker_back.utils;

import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeamMapperTest {

    @Test
    void toEntityCreate_success() {
        CreateTeamRequest request = new CreateTeamRequest("Main Backend",
                List.of("user1@email.com", "user3@email.com"), "user3@email.com");

        assertNotNull(request);
        assertEquals("Main Backend", request.getName());
        assertEquals(2, request.getCollaboratorsEmails().size());
        assertTrue(request.getCollaboratorsEmails().contains("user1@email.com"));
        assertTrue(request.getCollaboratorsEmails().contains("user3@email.com"));
        assertEquals("user3@email.com", request.getCreatorEmail());
    }

    @Test
    void toEntityCreate_null() {
        assertThrows(IllegalArgumentException.class, () -> {
            TeamMapper.toEntityCreate(null);
        });
    }


    @Test
    void toEntityUpdate_success() {
        UpdateTeamRequest request = new UpdateTeamRequest("id123", "Main Backend",
                List.of("user1@email.com", "user3@email.com"), "user3@email.com");

        assertNotNull(request);
        assertEquals("id123", request.getId());
        assertEquals("Main Backend", request.getName());
        assertEquals(2, request.getCollaboratorsEmails().size());
        assertTrue(request.getCollaboratorsEmails().contains("user1@email.com"));
        assertTrue(request.getCollaboratorsEmails().contains("user3@email.com"));
        assertEquals("user3@email.com", request.getCreatorEmail());
    }

    @Test
    void toEntityUpdate_null() {
        assertThrows(IllegalArgumentException.class, () -> {
            TeamMapper.toEntityUpdate(null);
        });
    }
}
