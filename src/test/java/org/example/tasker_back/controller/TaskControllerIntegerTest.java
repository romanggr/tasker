package org.example.tasker_back.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.tasker_back.IntegrationTest;
import org.example.tasker_back.config.TestConfig;
import org.example.tasker_back.dto.task.*;
import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.Role;
import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.model.Task;
import org.example.tasker_back.model.Team;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.TaskRepository;
import org.example.tasker_back.repository.TeamRepository;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
class TaskControllerIntegerTest extends IntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamRepository teamRepository;


    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
        teamRepository.deleteAll();

        User user1 = new User("user1", "user1@email.com", "User 1", BCrypt.hashpw("hashedPassword123", BCrypt.gensalt()), new ArrayList<>(List.of(Role.DESIGNER, Role.BIG_DATA)), new ArrayList<>(List.of("task1", "task2", "task3")), new ArrayList<>(List.of("team1")));
        User user2 = new User("user2", "user2@email.com", "User 2", BCrypt.hashpw("hashedPassword123", BCrypt.gensalt()), new ArrayList<>(List.of(Role.ACCOUNTANT, Role.DEVELOPER)), new ArrayList<>(List.of("task1", "task2", "task3")), new ArrayList<>(List.of("team1")));
        User user3 = new User("user3", "user3@email.com", "User 3", BCrypt.hashpw("hashedPassword123", BCrypt.gensalt()), new ArrayList<>(List.of(Role.DESIGNER, Role.BIG_DATA)), new ArrayList<>(List.of("task1", "task2", "task3")), new ArrayList<>(List.of("team1")));
        User user4 = new User("user4", "user4@email.com", "User 4", BCrypt.hashpw("hashedPassword123", BCrypt.gensalt()), new ArrayList<>(List.of(Role.DEVELOPER)), new ArrayList<>(List.of()), new ArrayList<>(List.of("team1", "team2", "team3")));
        userRepository.saveAll(List.of(user1, user2, user3, user4));

        Task task1 = new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team1", new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user1@email.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null);
        Task task2 = new Task("task2", "Create User Dashboard", "Develop a frontend dashboard for users to view their tasks and teams.", TaskStatus.FINISHED, Priority.MEDIUM, "team1", new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user2@email.com", LocalDateTime.of(2025, 2, 5, 10, 0), LocalDateTime.of(2025, 2, 10, 17, 0), LocalDateTime.of(2025, 2, 9, 15, 0));
        Task task3 = new Task("task3", "Fix Backend Bugs", "Identify and resolve critical bugs in the backend codebase.", TaskStatus.CREATED, Priority.LOW, "team1", new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user3@email.com", LocalDateTime.of(2025, 3, 12, 8, 30), LocalDateTime.of(2025, 3, 15, 18, 0), null);
        taskRepository.saveAll(List.of(task1, task2, task3));

        Team team1 = new Team("team1", "Original Team", new ArrayList<>(List.of(task1, task2, task3)), new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com", "user4@email.com")), "user1@email.com");
        teamRepository.save(team1);
    }

    @SneakyThrows
    @Test
    void getTasksFromTeam_success() {
        TasksRequest request = new TasksRequest("team1", "user1@email.com");
        String jwt = jwtService.generateToken("user1@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(get("http://localhost:1234/api/v1/task/teamTasks")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<Task> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(3);
    }

    @SneakyThrows
    @Test
    void getTasksFromTeamByStatus_success() {
        TasksByStatusRequest request = new TasksByStatusRequest("team1", "user1@email.com", TaskStatus.CREATED);
        String jwt = jwtService.generateToken("user1@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(get("http://localhost:1234/api/v1/task/teamTasks/status")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<Task> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
    }

    @SneakyThrows
    @Test
    void joinToTask_success() {
        JoinToTaskRequest request = new JoinToTaskRequest("user4@email.com", "task1", "team1");
        String jwt = jwtService.generateToken("user4@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(post("http://localhost:1234/api/v1/task/join")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Task response = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);
        assertThat(response).isNotNull();
        assertThat(response.getCollaboratorsEmails().contains(request.getUserEmail())).isTrue();

        User user = userRepository.findByEmail(request.getUserEmail()).get();
        assertThat(user.getTaskIds().contains(response.getId())).isTrue();
    }

    @SneakyThrows
    @Test
    void createTask_success() {
        CreateTaskRequest request = new CreateTaskRequest("team1", "Super task", "Random description", Priority.HIGHEST, List.of("user1@email.com", "user10@email.com", "user100@email.com"), "user2@email.com", LocalDateTime.of(2025, 1, 1, 9, 0));
        String jwt = jwtService.generateToken("user3@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(post("http://localhost:1234/api/v1/task/create")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Task response = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);

        assertThat(response).isNotNull();
        assertThat(response.getTeamId()).isEqualTo(request.getTeamId());
        assertThat(response.getCreatorEmail()).isEqualTo(request.getCreatorEmail());
        assertThat(response.getCollaboratorsEmails().size()).isEqualTo(2);
        assertThat(response.getTeamId()).isEqualTo(request.getTeamId());

        Team team = teamRepository.findById(response.getTeamId()).get();
        assertThat(team.getTasks().stream().anyMatch(task -> Objects.equals(task.getId(), response.getId()))).isTrue();

        List<User> userEmails = userRepository.findByEmailIn(response.getCollaboratorsEmails());
        assertThat(userEmails.size()).isEqualTo(2);
        for (User user : userEmails) {
            assertThat(user.getTaskIds().contains(response.getId())).isTrue();
        }
    }

    @Test
    @SneakyThrows
    void finishTask_success() {
        FinishTaskRequest request = new FinishTaskRequest("task1", "user1@email.com", "team1");
        String jwt = jwtService.generateToken("user1@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(put("http://localhost:1234/api/v1/task/finish")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Task response = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);

        assertThat(response).isNotNull();
        assertThat(response.getTeamId()).isEqualTo(request.getTeamId());
        assertThat(response.getFinishedAt()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(TaskStatus.FINISHED);
    }


    @Test
    @SneakyThrows
    void updateTask_success() {
        UpdateTaskRequest request = new UpdateTaskRequest("team1", "user1@email.com", "task1", "New description", Priority.HIGHEST, LocalDateTime.now());
        String jwt = jwtService.generateToken("user1@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(put("http://localhost:1234/api/v1/task/update")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Task response = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);

        assertThat(response).isNotNull();
        assertThat(response.getTeamId()).isEqualTo(request.getTeamId());
        assertThat(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(response.getPriority()).isEqualTo(request.getPriority());
        assertThat(response.getStartsAt()).isEqualTo(request.getStartsAt());
    }


    @Test
    @SneakyThrows
    void getTaskCollaborators_success() {
        String jwt = jwtService.generateToken("user1@email.com");

        var result = mvc.perform(get("http://localhost:1234/api/v1/task/collaborators/task1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(3);
    }
}