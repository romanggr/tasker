package org.example.tasker_back.service.team;

import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.DeleteTeamRequest;
import org.example.tasker_back.dto.team.LeaveTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.exceptions.EntityNotFoundException;
import org.example.tasker_back.model.Task;
import org.example.tasker_back.model.Team;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.TaskRepository;
import org.example.tasker_back.repository.TeamRepository;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.utils.TeamUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TeamUtils teamUtils;

    @InjectMocks
    private TeamServiceImpl teamService;


    @Test
    void getUserTeams_success() {
        String userEmail = "userEmail@email.com";
        Task task = new Task(
                "1",
                "Finish project report",
                "Complete the final report for the project.",
                TaskStatus.RUNNING,
                Priority.HIGH,
                "team-1",
                List.of("user1@email.com", "user2@email.com"),
                "creator@email.com",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3)
        );
        List<Team> teams = List.of(new Team("123", "Team A", List.of(task), List.of("user@example.com", "user2@example.com"), "user@example.com"), new Team("124", "Team B", List.of(task), List.of("user@example.com", "user2@example.com"), "user5@example.com"));

        when(teamRepository.findByCollaboratorsEmailsContaining(userEmail)).thenReturn(teams);

        List<Team> result = teamService.getUserTeams(userEmail);

        assertEquals(2, result.size());
        assertEquals("Team A", result.get(0).getName());
        assertEquals("Team B", result.get(1).getName());

        verify(teamRepository, times(1)).findByCollaboratorsEmailsContaining(userEmail);
    }

    @Test
    void getUserTeams_noTeams() {
        String userEmail = "user@example.com";

        when(teamRepository.findByCollaboratorsEmailsContaining(userEmail)).thenReturn(Collections.emptyList());

        List<Team> result = teamService.getUserTeams(userEmail);

        assertTrue(result.isEmpty());
        verify(teamRepository, times(1)).findByCollaboratorsEmailsContaining(userEmail);
    }


    @Test
    void createTeam_success() {
        List<String> collaborators = new ArrayList<>(List.of("email1@gmail.com", "email2@gmail.com"));
        CreateTeamRequest request = new CreateTeamRequest("Main team", collaborators, "creator@gmail.com");

        Team team = new Team();
        team.setId("team123");
        team.setName("Main team");
        team.setCollaboratorsEmails(List.of("email1@gmail.com", "email2@gmail.com", "creator@gmail.com"));
        team.setCreatorEmail("creator@gmail.com");

        User user1 = new User();
        user1.setEmail("email1@gmail.com");
        user1.setTeamIds(new ArrayList<>());

        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(userRepository.findByEmailIn(anyList())).thenReturn(List.of(user1));

        Team result = teamService.createTeam(request);

        verify(teamRepository, times(1)).save(any(Team.class));
        verify(userRepository, times(1)).findByEmailIn(anyList());
        verify(teamUtils, times(1)).validateCreateTeam(request);

        assertNotNull(result);
        assertEquals("Main team", result.getName());
        assertEquals(3, result.getCollaboratorsEmails().size());
        assertTrue(result.getCollaboratorsEmails().contains("creator@gmail.com"));
        assertEquals("creator@gmail.com", result.getCreatorEmail());

        assertEquals(1, user1.getTeamIds().size());
        assertEquals("team123", user1.getTeamIds().get(0));
    }


    @Test
    void createTeam_validationException() {
        List<String> collaborators = new ArrayList<>(List.of("email1@gmail.com", "email2@gmail.com"));
        CreateTeamRequest request = new CreateTeamRequest(null, collaborators, "creator@gmail.com");

        Mockito.doThrow(new IllegalArgumentException("Name cannot be empty or null"))
                .when(teamUtils).validateCreateTeam(request);

        assertThrows(IllegalArgumentException.class, () -> teamService.createTeam(request));

        verify(teamRepository, never()).save(any(Team.class));
        verify(userRepository, never()).findByEmailIn(anyList());
        verify(teamUtils, times(1)).validateCreateTeam(request);
    }


    @Test
    void updateTeam_success() {
        UpdateTeamRequest request = new UpdateTeamRequest(
                "team123",
                "Updated Team",
                new ArrayList<>(List.of("user1@gmail.com", "user2@gmail.com")),
                "creator@gmail.com"
        );

        Team dbTeam = new Team(
                "team123",
                "Original Team",
                new ArrayList<>(),
                new ArrayList<>(List.of("user1@gmail.com", "test1@gmail.com")),
                "creator@gmail.com"
        );

        List<User> usersFromTeam = List.of(
                new User("1", "test1@gmail.com", "Test User 1", "password123", List.of(), null, List.of("team123")),
                new User("2", "user1@gmail.com", "User 1", "password123", List.of(), null, List.of("team123")),
                new User("3", "creator@gmail.com", "Creator", "password123", List.of(), null, List.of("team123"))
        );

        when(teamRepository.findById(request.getId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByTeamIdsContaining(dbTeam.getId())).thenReturn(usersFromTeam);
        when(userRepository.findByEmail("user2@gmail.com")).thenReturn(Optional.of(
                new User("4", "user2@gmail.com", "User 2", "password123", List.of(), null, new ArrayList<>())
        ));

        Team updatedTeam = teamService.updateTeam(request);

        assertNotNull(updatedTeam);
        assertEquals("Updated Team", updatedTeam.getName());
        assertTrue(updatedTeam.getCollaboratorsEmails().contains("user1@gmail.com"));
        assertTrue(updatedTeam.getCollaboratorsEmails().contains("user2@gmail.com"));
        assertTrue(updatedTeam.getCollaboratorsEmails().contains("creator@gmail.com"));

        verify(teamRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByTeamIdsContaining(anyString());
        verify(userRepository, times(1)).findByEmail("user2@gmail.com");
        verify(userRepository, times(2)).save(any(User.class));
        verify(teamRepository, times(1)).save(any(Team.class));
    }


    @Test
    void updateTeam_invalidCreator() {
        UpdateTeamRequest request = new UpdateTeamRequest(
                "team123",
                "Updated Team",
                new ArrayList<>(List.of("user1@gmail.com", "user2@gmail.com")),
                "creator@gmail.com"
        );

        Team dbTeam = new Team(
                "team123",
                "Original Team",
                new ArrayList<>(),
                new ArrayList<>(List.of("user1@gmail.com", "test1@gmail.com")),
                "other@gmail.com"
        );

        when(teamRepository.findById(request.getId())).thenReturn(Optional.of(dbTeam));

        assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(request));
    }

    @Test
    void updateTeam_invalidTeamId() {
        UpdateTeamRequest request = new UpdateTeamRequest(
                "team123",
                "Updated Team",
                new ArrayList<>(List.of("user1@gmail.com", "user2@gmail.com")),
                "creator@gmail.com"
        );


        when(teamRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.updateTeam(request));
    }

    @Test
    void updateTeam_userNotFound() {
        UpdateTeamRequest request = new UpdateTeamRequest(
                "team123",
                "Updated Team",
                new ArrayList<>(List.of("user1@gmail.com", "user2@gmail.com")),
                "creator@gmail.com"
        );

        Team dbTeam = new Team(
                "team123",
                "Original Team",
                new ArrayList<>(),
                new ArrayList<>(List.of("user1@gmail.com", "test1@gmail.com")),
                "creator@gmail.com"
        );

        List<User> usersFromTeam = List.of(
                new User("1", "test1@gmail.com", "Test User 1", "password123", List.of(), null, List.of("team123")),
                new User("2", "user1@gmail.com", "User 1", "password123", List.of(), null, List.of("team123")),
                new User("3", "creator@gmail.com", "Creator", "password123", List.of(), null, List.of("team123"))
        );

        when(teamRepository.findById(request.getId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByTeamIdsContaining(dbTeam.getId())).thenReturn(usersFromTeam);
        when(userRepository.findByEmail("user2@gmail.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.updateTeam(request));

        verify(teamRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByTeamIdsContaining(anyString());
        verify(userRepository, times(1)).findByEmail("user2@gmail.com");
    }


    @Test
    void deleteTeam_success() {
        List<Task> tasks = List.of(
                new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team123", List.of("test1@gmail.com", "creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null),
                new Task("task2", "Write Documentation", "Write API documentation for the project using Swagger", TaskStatus.RUNNING, Priority.LOW, "team123", List.of("test1@gmail.com", "creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 2, 10, 0), LocalDateTime.of(2025, 1, 1, 16, 0), null)
        );
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                tasks,
                new ArrayList<>(List.of("user1@gmail.com", "test1@gmail.com")),
                "creator@gmail.com");
        DeleteTeamRequest request = new DeleteTeamRequest("team123", "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByEmailIn(dbTeam.getCollaboratorsEmails())).thenReturn(List.of(
                new User("1", "test1@gmail.com", "Test User 1", "password123", List.of(), new ArrayList<>(), List.of("team123")),
                new User("2", "user1@gmail.com", "User 1", "password123", List.of(), null, List.of("team123"))
        ));


        teamService.deleteTeam(request);

        verify(teamRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByEmailIn(anyList());
        verify(userRepository, times(1)).saveAll(anyList());
        verify(teamRepository, times(1)).delete(any(Team.class));
        verify(taskRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void deleteTeam_invalidCreator() {
        List<Task> tasks = List.of(
                new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team123", List.of("test1@gmail.com", "creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null),
                new Task("task2", "Write Documentation", "Write API documentation for the project using Swagger", TaskStatus.RUNNING, Priority.LOW, "team123", List.of("test1@gmail.com", "creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 2, 10, 0), LocalDateTime.of(2025, 1, 1, 16, 0), null)
        );
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                tasks,
                new ArrayList<>(List.of("user1@gmail.com", "test1@gmail.com")),
                "creator1@gmail.com");
        DeleteTeamRequest request = new DeleteTeamRequest("team123", "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));

        assertThrows(IllegalArgumentException.class, () -> teamService.deleteTeam(request));

        verify(teamRepository, times(1)).findById(anyString());
    }

    @Test
    void deleteTeam_invalidId() {
        DeleteTeamRequest request = new DeleteTeamRequest("team123", "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.deleteTeam(request));
        verify(teamRepository, times(1)).findById(anyString());
    }

    @Test
    void deleteTeam_emptyTasks() {
        List<Task> tasks = List.of();
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                tasks,
                new ArrayList<>(List.of("user1@gmail.com", "test1@gmail.com")),
                "creator@gmail.com");
        DeleteTeamRequest request = new DeleteTeamRequest("team123", "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByEmailIn(dbTeam.getCollaboratorsEmails())).thenReturn(List.of(
                new User("1", "test1@gmail.com", "Test User 1", "password123", List.of(), new ArrayList<>(), List.of("team123")),
                new User("2", "user1@gmail.com", "User 1", "password123", List.of(), null, List.of("team123"))
        ));


        teamService.deleteTeam(request);

        verify(teamRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByEmailIn(anyList());
        verify(userRepository, times(1)).saveAll(anyList());
        verify(teamRepository, times(1)).delete(any(Team.class));
        verify(taskRepository, never()).deleteAll(anyList());
    }

    @Test
    void leaveTeam_success_notCreator() {
        List<Task> tasks = List.of(
                new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team123", List.of("test1@gmail.com", "creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null),
                new Task("task2", "Write Documentation", "Write API documentation for the project using Swagger", TaskStatus.RUNNING, Priority.LOW, "team123", List.of("test1@gmail.com", "creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 2, 10, 0), LocalDateTime.of(2025, 1, 1, 16, 0), null)
        );
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                tasks,
                new ArrayList<>(List.of("user1@gmail.com", "test1@gmail.com", "creator@gmail.com")),
                "creator@gmail.com");

        LeaveTeamRequest request = new LeaveTeamRequest("test1@gmail.com", "team123");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByEmailIn(dbTeam.getCollaboratorsEmails())).thenReturn(List.of(
                new User("1", "test1@gmail.com", "Test User 1", "password123", List.of(), new ArrayList<>(), List.of("team123")),
                new User("2", "user1@gmail.com", "User 1", "password123", List.of(), null, List.of("team123"))));
        when(taskRepository.findByCollaboratorsEmail(anyString())).thenReturn(tasks);
        when(teamRepository.findByCollaboratorsEmailsContaining(anyString())).thenReturn(List.of(dbTeam));


        teamService.leaveTeam(request);

        verify(teamRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByEmailIn(anyList());
        verify(teamRepository, times(1)).findByCollaboratorsEmailsContaining(anyString());
        verify(teamRepository, times(1)).saveAll(anyList());
        verify(taskRepository, times(1)).findByCollaboratorsEmail(anyString());
        verify(taskRepository, times(1)).saveAll(anyList());
    }

    @Test
    void leaveTeam_success_creator() {
        List<Task> tasks = List.of(
                new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team123", List.of("test1@gmail.com", "creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null),
                new Task("task2", "Write Documentation", "Write API documentation for the project using Swagger", TaskStatus.RUNNING, Priority.LOW, "team123", List.of("test1@gmail.com", "creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 2, 10, 0), LocalDateTime.of(2025, 1, 1, 16, 0), null)
        );
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                tasks,
                new ArrayList<>(List.of("user1@gmail.com", "test1@gmail.com", "creator@gmail.com")),
                "creator@gmail.com");

        LeaveTeamRequest request = new LeaveTeamRequest("creator@gmail.com", "team123");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByEmailIn(dbTeam.getCollaboratorsEmails())).thenReturn(List.of(
                new User("1", "test1@gmail.com", "Test User 1", "password123", List.of(), new ArrayList<>(), List.of("team123")),
                new User("2", "user1@gmail.com", "User 1", "password123", List.of(), null, List.of("team123"))));
        when(taskRepository.findByCollaboratorsEmail(anyString())).thenReturn(tasks);
        when(teamRepository.findByCollaboratorsEmailsContaining(anyString())).thenReturn(List.of(dbTeam));


        teamService.leaveTeam(request);

        verify(teamRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByEmailIn(anyList());
        verify(teamRepository, times(1)).findByCollaboratorsEmailsContaining(anyString());
        verify(teamRepository, times(1)).saveAll(anyList());
        verify(taskRepository, times(1)).findByCollaboratorsEmail(anyString());
        verify(taskRepository, times(1)).saveAll(anyList());
    }

    @Test
    void leaveTeam_success_creatorOnlyOneCollaborator() {
        List<Task> tasks = List.of(
                new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team123", List.of("creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null),
                new Task("task2", "Write Documentation", "Write API documentation for the project using Swagger", TaskStatus.RUNNING, Priority.LOW, "team123", List.of("creator@gmail.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 2, 10, 0), LocalDateTime.of(2025, 1, 1, 16, 0), null)
        );
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                tasks,
                new ArrayList<>(List.of("creator@gmail.com")),
                "creator@gmail.com");

        LeaveTeamRequest request = new LeaveTeamRequest("creator@gmail.com", "team123");


        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByEmailIn(dbTeam.getCollaboratorsEmails())).thenReturn(List.of(
                new User("1", "creator@gmail.com", "Test User 1", "password123", List.of(), new ArrayList<>(), List.of("team123"))
        ));

        teamService.leaveTeam(request);

        verify(teamRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByEmailIn(anyList());
        verify(teamRepository, times(1)).delete(any(Team.class));
        verify(userRepository, times(1)).saveAll(anyList());
        verify(taskRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void leaveTeam_invalidId() {
        LeaveTeamRequest request = new LeaveTeamRequest("creator@gmail.com", "team123");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.leaveTeam(request));
    }
}