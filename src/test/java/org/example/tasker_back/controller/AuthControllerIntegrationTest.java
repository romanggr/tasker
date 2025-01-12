package org.example.tasker_back.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.tasker_back.IntegrationTest;
import org.example.tasker_back.config.TestConfig;
import org.example.tasker_back.dto.user.AuthResponse;
import org.example.tasker_back.dto.user.LoginRequest;
import org.example.tasker_back.dto.user.RegistrationRequest;
import org.example.tasker_back.enums.Role;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TestConfig.class)
public class AuthControllerIntegrationTest extends IntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }


    @Test
    @SneakyThrows
    void registration_success() {
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "password123",
                "test@gmail.com",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );

        String requestJson = objectMapper.writeValueAsString(request);

        var result = mvc.perform(post("http://localhost:1234/api/v1/auth/registration")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        AuthResponse authResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponse.class);

        assertThat(authResponse).isNotNull();
        assertThat(jwtService.isTokenValid(authResponse.getToken(), request.getEmail())).isTrue();
        assertThat(authResponse.getUserDto().getId()).isNotNull();
        assertThat(authResponse.getUserDto().getEmail()).isEqualTo(request.getEmail());
        assertThat(authResponse.getUserDto().getFullName()).isEqualTo(request.getFullName());
        assertThat(authResponse.getUserDto().getRoles().size()).isEqualTo(request.getRoles().size());

        User userDb = userRepository.findAll().get(0);
        assertThat(userDb.getEmail()).isEqualTo(request.getEmail());
        assertThat(userDb.getFullName()).isEqualTo(request.getFullName());
        assertThat(userDb.getRoles().size()).isEqualTo(request.getRoles().size());
        assertThat(BCrypt.checkpw(request.getPassword(), userDb.getPassword())).isTrue();
    }


    @Test
    @SneakyThrows
    void registration_invalidEmail() {
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "password123",
                "testcom",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );

        String requestJson = objectMapper.writeValueAsString(request);
        mvc.perform(post("http://localhost:1234/api/v1/auth/registration")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void registration_takenEmail() {
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "password123",
                "test@gmail.com",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );

        User user = new User(null, "test@gmail.com", "Karol", "password123", List.of(), null, null);
        userRepository.save(user);

        String requestJson = objectMapper.writeValueAsString(request);
        mvc.perform(post("http://localhost:1234/api/v1/auth/registration")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @SneakyThrows
    void login_success() {
        LoginRequest request = new LoginRequest("test@gmail.com","password123");

        User user = new User(null, "test@gmail.com", "Karol", BCrypt.hashpw("password123", BCrypt.gensalt()), List.of(), null, null);
        userRepository.save(user);


        String requestJson = objectMapper.writeValueAsString(request);
        var result = mvc.perform(post("http://localhost:1234/api/v1/auth/login")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        AuthResponse authResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponse.class);

        assertThat(authResponse).isNotNull();
        assertThat(jwtService.isTokenValid(authResponse.getToken(), request.getEmail())).isTrue();
        assertThat(authResponse.getUserDto().getId()).isNotNull();
        assertThat(authResponse.getUserDto().getEmail()).isEqualTo(request.getEmail());
        assertThat(authResponse.getUserDto().getFullName()).isEqualTo(user.getFullName());
        assertThat(authResponse.getUserDto().getRoles().size()).isEqualTo(user.getRoles().size());
    }


    @Test
    @SneakyThrows
    void login_invalidPassword() {
        LoginRequest request = new LoginRequest("test@gmail.com","password");

        User user = new User(null, "test@gmail.com", "Karol", BCrypt.hashpw("password123", BCrypt.gensalt()), List.of(), null, null);
        userRepository.save(user);


        String requestJson = objectMapper.writeValueAsString(request);
        mvc.perform(post("http://localhost:1234/api/v1/auth/login")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
