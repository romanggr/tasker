package org.example.tasker_back.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.tasker_back.IntegrationTest;
import org.example.tasker_back.config.TestConfig;
import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.example.tasker_back.dto.team.DeleteTeamRequest;
import org.example.tasker_back.dto.team.LeaveTeamRequest;
import org.example.tasker_back.dto.team.UpdateTeamRequest;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
class TeamControllerIntegrationTest extends IntegrationTest {
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
        userRepository.saveAll(List.of(user1, user2, user3));

        Task task1 = new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team1", new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user1@email.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null);
        Task task2 = new Task("task2", "Create User Dashboard", "Develop a frontend dashboard for users to view their tasks and teams.", TaskStatus.FINISHED, Priority.MEDIUM, "team1", new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user2@email.com", LocalDateTime.of(2025, 2, 5, 10, 0), LocalDateTime.of(2025, 2, 10, 17, 0), LocalDateTime.of(2025, 2, 9, 15, 0));
        Task task3 = new Task("task3", "Fix Backend Bugs", "Identify and resolve critical bugs in the backend codebase.", TaskStatus.CREATED, Priority.LOW, "team1", new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user3@email.com", LocalDateTime.of(2025, 3, 12, 8, 30), LocalDateTime.of(2025, 3, 15, 18, 0), null);
        taskRepository.saveAll(List.of(task1, task2, task3));

        Team team1 = new Team("team1", "Original Team1", new ArrayList<>(List.of(task1, task2, task3)), new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user1@email.com");
        Team team2 = new Team("team2", "Original Team2", new ArrayList<>(List.of(task1, task2, task3)), new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user1@email.com");
        Team team3 = new Team("team3", "Original Team3", new ArrayList<>(List.of(task1, task2)), new ArrayList<>(List.of("user1@email.com", "user2@email.com", "user3@email.com")), "user1@email.com");
        teamRepository.saveAll(List.of(team1, team2, team3));
    }

    @SneakyThrows
    @Test
    void getUserTeams_success() {
        String jwt = jwtService.generateToken("user1@email.com");

        var result = mvc.perform(get("http://localhost:1234/api/v1/team/user1@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<Team> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(3);
    }

    @Test
    @SneakyThrows
    void createTeam_success() {
        CreateTeamRequest request = new CreateTeamRequest("team4", List.of("user1@email.com"), "user2@email.com");
        String jwt = jwtService.generateToken("user2@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(post("http://localhost:1234/api/v1/team/create")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Team response = objectMapper.readValue(result.getResponse().getContentAsString(), Team.class);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getCollaboratorsEmails().size()).isEqualTo(2);
        assertThat(response.getCreatorEmail()).isEqualTo(request.getCreatorEmail());

        List<User> users = userRepository.findByTeamIdsContaining(response.getId());
        assertThat(users.size()).isEqualTo(2);
        for (User user : users) {
            assertThat(user.getTeamIds().contains(response.getId())).isTrue();
        }
    }

    @SneakyThrows
    @Test
    void updateTeam_success() {
        UpdateTeamRequest request = new UpdateTeamRequest("team1", "new name", List.of("user2@email.com"), "user1@email.com");
        String jwt = jwtService.generateToken("user1@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(put("http://localhost:1234/api/v1/team/update")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Team response = objectMapper.readValue(result.getResponse().getContentAsString(), Team.class);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getCollaboratorsEmails().size()).isEqualTo(2);

        List<User> users = userRepository.findByTeamIdsContaining(response.getId());
        assertThat(users.size()).isEqualTo(2);
    }


    @Test
    @SneakyThrows
    void deleteTeam_success() {
        DeleteTeamRequest request = new DeleteTeamRequest("team3", "user1@email.com");
        String jwt = jwtService.generateToken("user1@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(delete("http://localhost:1234/api/v1/team/delete")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo("Team deleted successfully");

        List<Team> teams = teamRepository.findAll();
        assertThat(teams.size()).isEqualTo(2);

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks.size()).isEqualTo(1);


        List<User> users = userRepository.findAll();
        for (User user : users) {
            assertThat(user.getTeamIds().contains(request.getTeamId())).isFalse();
            assertThat(user.getTaskIds().contains("task1")).isFalse();
            assertThat(user.getTaskIds().contains("task2")).isFalse();
        }
    }

    @SneakyThrows
    @Test
    void leaveTeam_success() {
        LeaveTeamRequest request = new LeaveTeamRequest("user2@email.com", "team3");
        String jwt = jwtService.generateToken("user1@email.com");

        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(put("http://localhost:1234/api/v1/team/leave")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo("Team left successfully");

        Team team = teamRepository.findById("team3").get();
        assertThat(team.getCollaboratorsEmails().size()).isEqualTo(2);

        User user = userRepository.findByEmail("user2@email.com").get();
        assertThat(user.getTeamIds().contains(request.getTeamId())).isFalse();
        assertThat(user.getTaskIds().contains("task1")).isFalse();
        assertThat(user.getTaskIds().contains("task2")).isFalse();
    }
}