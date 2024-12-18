package org.example.tasker_back.service.auth;

import org.example.tasker_back.dto.user.AuthResponse;
import org.example.tasker_back.dto.user.LoginRequest;
import org.example.tasker_back.dto.user.RegistrationRequest;
import org.example.tasker_back.enums.Role;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.security.JwtService;
import org.example.tasker_back.service.user.auth.AuthServiceImpl;
import org.example.tasker_back.utils.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    void register_success() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "password123",
                "test@gmail.com",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );
        User user = UserMapper.createUser(request);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user.getEmail())).thenReturn("mockedToken");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("mockedToken", response.getToken());
        assertEquals(request.getEmail(), response.getUserDto().getEmail());

        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(user.getEmail());
    }

    @Test
    void register_invalidEmail() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "password123",
                "testEmail",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );
        User user = UserMapper.createUser(request);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));

        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(user.getEmail());
    }


    @Test
    void login_success() {
        LoginRequest request = new LoginRequest("testEmail@email.com", "password123");

        User userFromDb = new User();
        userFromDb.setEmail("testEmail@email.com");
        userFromDb.setPassword(BCrypt.hashpw("password123", BCrypt.gensalt()));

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userFromDb));
        when(jwtService.generateToken(userFromDb.getEmail())).thenReturn("mockedToken");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mockedToken", response.getToken());
        assertEquals(request.getEmail(), response.getUserDto().getEmail());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(jwtService, times(1)).generateToken(userFromDb.getEmail());
    }

    @Test
    void login_emailNotFound() {
        LoginRequest request = new LoginRequest("testEmail@email.com", "password123");

        User userFromDb = new User();
        userFromDb.setEmail("other@email.com");
        userFromDb.setPassword(BCrypt.hashpw("password123", BCrypt.gensalt()));

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(jwtService, never()).generateToken(userFromDb.getEmail());
    }

    @Test
    void login_invalidPassword() {
        LoginRequest request = new LoginRequest("testEmail@email.com", "invalidPassword");

        User userFromDb = new User();
        userFromDb.setEmail("testEmail@email.com");
        userFromDb.setPassword(BCrypt.hashpw("password123", BCrypt.gensalt()));

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userFromDb));

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(jwtService, never()).generateToken(userFromDb.getEmail());
    }




}