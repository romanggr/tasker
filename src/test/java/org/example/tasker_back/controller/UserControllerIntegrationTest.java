package org.example.tasker_back.controller;

import lombok.SneakyThrows;
import org.example.tasker_back.IntegrationTest;
import org.example.tasker_back.config.TestConfig;
import org.example.tasker_back.dto.user.AuthResponse;
import org.example.tasker_back.dto.user.UpdatePasswordRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TestConfig.class)
class UserControllerIntegrationTest extends IntegrationTest {
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
    }


    @SneakyThrows
    @Test
    void updatePassword_success() {
        String currentRawPassword = "password123";
        String newRawPassword = "newPassword123";
        User userDb = userRepository.save(new User(null, "test@gmail.com", "Karol", BCrypt.hashpw(currentRawPassword, BCrypt.gensalt()), List.of(), null, null));
        UpdatePasswordRequest request = new UpdatePasswordRequest(userDb.getId(), newRawPassword, currentRawPassword);
        String jwt = jwtService.generateToken("test@gmail.com");


        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(put("http://localhost:1234/api/v1/user/updatePassword")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        AuthResponse authResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponse.class);

        assertThat(authResponse).isNotNull();
        assertThat(jwtService.isTokenValid(authResponse.getToken(), userDb.getEmail())).isTrue();
        assertThat(authResponse.getUserDto().getId()).isNotNull();
        assertThat(authResponse.getUserDto().getEmail()).isEqualTo(userDb.getEmail());
        assertThat(authResponse.getUserDto().getFullName()).isEqualTo(userDb.getFullName());
        assertThat(authResponse.getUserDto().getRoles().size()).isEqualTo(userDb.getRoles().size());

        User updatedUser = userRepository.findAll().get(0);
        assertThat(BCrypt.checkpw(newRawPassword, updatedUser.getPassword())).isTrue();
    }


    @SneakyThrows
    @Test
    void updateUser() {
        String jwt = jwtService.generateToken("test@gmail.com");
        Team teamDb = teamRepository.save(new Team(null, "Original Team", List.of(),
                new ArrayList<>(List.of("test@gmail.com")), "test@gmail.com"));

        Task taskDb = taskRepository.save(new Task(null, "Implement Authentication",
                "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH,
                teamDb.getId(), new ArrayList<>(List.of("test@gmail.com")), "test@gmail.com",
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2024, 12, 31, 15, 0), null));

        User userDb = userRepository.save(new User(null, "test@gmail.com", "Karol",
                BCrypt.hashpw("currentRawPassword", BCrypt.gensalt()), List.of(Role.BIG_DATA), List.of(taskDb.getId()), List.of(teamDb.getId())));

        UpdateUserRequest request = new UpdateUserRequest(userDb.getId(), "test123@gmail.com", "Roman", List.of(Role.DESIGNER, Role.ACCOUNTANT));
        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(put("http://localhost:1234/api/v1/user/updateUser")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        AuthResponse authResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponse.class);

        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getUserDto().getEmail()).isEqualTo(request.getEmail());
        assertThat(authResponse.getUserDto().getFullName()).isEqualTo(request.getFullName());
        assertThat(authResponse.getUserDto().getRoles().size()).isEqualTo(request.getRoles().size());


        Task updatedTask = taskRepository.findAll().get(0);
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getId()).isEqualTo(taskDb.getId());
        assertThat(updatedTask.getCreatorEmail()).isEqualTo(request.getEmail());
        assertThat(updatedTask.getCollaboratorsEmails().contains(request.getEmail())).isTrue();

        Team updatedTeam = teamRepository.findAll().get(0);
        assertThat(updatedTeam).isNotNull();
        assertThat(updatedTeam.getId()).isEqualTo(teamDb.getId());
        assertThat(updatedTeam.getCreatorEmail()).isEqualTo(request.getEmail());
        assertThat(updatedTeam.getCollaboratorsEmails().contains(request.getEmail())).isTrue();
    }

}