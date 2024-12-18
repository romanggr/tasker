package org.example.tasker_back.service.user;

import org.example.tasker_back.dto.user.AuthResponse;
import org.example.tasker_back.dto.user.UpdateUserRequest;
import org.example.tasker_back.enums.Role;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.TaskRepository;
import org.example.tasker_back.repository.TeamRepository;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.security.JwtService;
import org.example.tasker_back.service.user.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void updateUser_success() {
        User testUser = new User("123", "testEmail@email.com",
                "User 1", "password123", List.of(Role.DESIGNER, Role.BIG_DATA), List.of("1a233"), List.of("ewe233"));

        UpdateUserRequest request = new UpdateUserRequest("123", "newEmail@gmail.com", "Romik");

        when(userRepository.findById(request.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(request.getEmail())).thenReturn("jwt");

        AuthResponse response = userService.updateUser(request);

        assertEquals("jwt", response.getToken());
        assertEquals("newEmail@gmail.com", response.getUserDto().getEmail());
        assertEquals("Romik", response.getUserDto().getFullName());

        verify(userRepository, times(1)).findById(request.getId());
        verify(userRepository, times(2)).save(any());
        verify(jwtService, times(1)).generateToken(request.getEmail());
        verify(teamRepository, times(1)).findAllById(testUser.getTeamIds());
        verify(teamRepository, times(1)).saveAll(any());
        verify(taskRepository, times(1)).findAllById(testUser.getTaskIds());
        verify(taskRepository, times(1)).saveAll(any());
    }

    @Test
    void updateUser_invalidUserId() {
        UpdateUserRequest request = new UpdateUserRequest("123", "newEmail@gmail.com", "Romik");

        when(userRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(request));

        verify(userRepository, times(1)).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_invalidEmail() {
        UpdateUserRequest request = new UpdateUserRequest("123", "newEmail", "Romik");

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(request));

        verify(userRepository, times(1)).findById(any());
        verify(userRepository, never()).save(any());
    }


    @Test
    void updateUser_nullTeamIdsAndTasks() {
        User testUser = new User("123", "testEmail@email.com",
                "User 1", "password123", List.of(Role.DESIGNER, Role.BIG_DATA), null, null);

        UpdateUserRequest request = new UpdateUserRequest("123", "newEmail@gmail.com", "Romik");

        when(userRepository.findById(request.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(request.getEmail())).thenReturn("jwt");

        AuthResponse response = userService.updateUser(request);

        assertEquals("jwt", response.getToken());
        assertEquals("newEmail@gmail.com", response.getUserDto().getEmail());
        assertEquals("Romik", response.getUserDto().getFullName());

        verify(userRepository, times(1)).findById(request.getId());
        verify(userRepository, times(2)).save(any());
        verify(jwtService, times(1)).generateToken(request.getEmail());

        verify(teamRepository, never()).findAllById(testUser.getTeamIds());
        verify(teamRepository, never()).saveAll(any());
        verify(taskRepository, never()).findAllById(testUser.getTaskIds());
        verify(taskRepository, never()).saveAll(any());
    }


    @Test
    void updateUser_sameEmail() {
        User testUser = new User("123", "testEmail@email.com",
                "User 1", "password123", List.of(Role.DESIGNER, Role.BIG_DATA), null, null);

        UpdateUserRequest request = new UpdateUserRequest("123", "testEmail@email.com", "Romik");

        when(userRepository.findById(request.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(request.getEmail())).thenReturn("jwt");

        AuthResponse response = userService.updateUser(request);

        assertEquals("jwt", response.getToken());
        assertEquals("testEmail@email.com", response.getUserDto().getEmail());
        assertEquals("Romik", response.getUserDto().getFullName());

        verify(userRepository, times(1)).findById(request.getId());
        verify(userRepository, times(2)).save(any());
        verify(jwtService, times(1)).generateToken(request.getEmail());

        verify(teamRepository, never()).findAllById(testUser.getTeamIds());
        verify(teamRepository, never()).saveAll(any());
        verify(taskRepository, never()).findAllById(testUser.getTaskIds());
        verify(taskRepository, never()).saveAll(any());
    }

}