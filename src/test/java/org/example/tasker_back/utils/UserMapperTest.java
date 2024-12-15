package org.example.tasker_back.utils;

import org.example.tasker_back.dto.user.RegistrationRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;
import org.example.tasker_back.dto.user.UserDTO;
import org.example.tasker_back.enums.Role;
import org.example.tasker_back.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {


    @Test
    void createUser_success() {
        RegistrationRequest request = new RegistrationRequest("John Doe",
                "password123", "john.doe@example.com", List.of(Role.ACCOUNTANT, Role.DESIGNER));

        User newUser = UserMapper.createUser(request);

        assertNotNull(newUser);
        assertEquals("john.doe@example.com", newUser.getEmail());
        assertEquals("John Doe", newUser.getFullName());
        assertEquals(2, newUser.getRoles().size());
        assertTrue(newUser.getRoles().contains(Role.ACCOUNTANT));
    }

    @Test
    void createUser_nullRequest() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserMapper.createUser(null);
        });
    }


    @Test
    void toDto_success() {
        User newUser = new User("id1234", "john.doe@example.com",
                "John Doe", "password123", List.of(Role.ACCOUNTANT, Role.DESIGNER),
                List.of("idTask1", "idTask2"), List.of("team1", "team31")
        );

        UserDTO dto = UserMapper.toDTO(newUser);

        assertNotNull(dto);
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals("John Doe", dto.getFullName());
        assertEquals(2, dto.getRoles().size());
        assertTrue(dto.getRoles().contains(Role.ACCOUNTANT));
        assertEquals(2, dto.getTaskIds().size());
        assertTrue(dto.getTaskIds().contains("idTask1"));
        assertTrue(dto.getTaskIds().contains("idTask2"));
        assertEquals(2, dto.getTeamIds().size());
        assertTrue(dto.getTeamIds().contains("team1"));
        assertTrue(dto.getTeamIds().contains("team31"));
    }

    @Test
    void toDto_nullRequest() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserMapper.toDTO(null);
        });
    }

    @Test
    void updateUser_success() {
        User user = new User("id1234", "john.doe@example.com", "John Doe", "password123",
                List.of(Role.ACCOUNTANT, Role.DESIGNER), List.of("idTask1", "idTask2"), List.of("team1", "team31"));

        UpdateUserRequest request = new UpdateUserRequest("id123", "doe@example.com", "Doe John");

        User updatedUser = UserMapper.updateUser(request, user);


        assertNotNull(updatedUser);
        assertEquals("doe@example.com", updatedUser.getEmail());
        assertEquals("Doe John", updatedUser.getFullName());
        assertEquals(2, updatedUser.getRoles().size());
        assertTrue(updatedUser.getRoles().contains(Role.ACCOUNTANT));
        assertEquals(2, updatedUser.getTaskIds().size());
        assertTrue(updatedUser.getTaskIds().contains("idTask1"));
        assertTrue(updatedUser.getTaskIds().contains("idTask2"));
        assertEquals(2, updatedUser.getTeamIds().size());
        assertTrue(updatedUser.getTeamIds().contains("team1"));
        assertTrue(updatedUser.getTeamIds().contains("team31"));
    }

    @Test
    void updateUser_nullRequest() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserMapper.updateUser(null, new User());
        });
    }


}
