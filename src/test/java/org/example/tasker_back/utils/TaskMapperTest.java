package org.example.tasker_back.utils;

import org.example.tasker_back.dto.task.CreateTaskRequest;
import org.example.tasker_back.dto.task.UpdateTaskRequest;
import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.model.Task;
import org.example.tasker_back.model.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskMapperTest {
    @Test
    void toEntityCreate_success() {
        CreateTaskRequest request = new CreateTaskRequest("id123", "Main Backend",
                "description", Priority.HIGH, List.of("user1@email.com", "user3@email.com"),
                "user3@email.com", LocalDateTime.now());

        Task task = TaskMapper.toEntityCreate(request);

        assertNotNull(task);
        assertEquals("id123", task.getTeamId());
        assertEquals("Main Backend", task.getName());
        assertEquals("description", task.getDescription());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(2, task.getCollaboratorsEmails().size());
        assertTrue(task.getCollaboratorsEmails().contains("user1@email.com"));
        assertTrue(task.getCollaboratorsEmails().contains("user3@email.com"));
        assertEquals("user3@email.com", task.getCreatorEmail());
        assertNotNull(task.getStartsAt());
    }

    @Test
    void toEntityCreate_null() {
        assertThrows(IllegalArgumentException.class, () -> {
            TaskMapper.toEntityCreate(null);
        });
    }

    @Test
    void toEntityCreate_name_null() {
        CreateTaskRequest request = new CreateTaskRequest("id123", "Main Backend",
                "description", Priority.HIGH, List.of("user1@email.com", "user3@email.com"),
                "user3@email.com", LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> {
            TaskMapper.toEntityCreate(request);
        });
    }

    @Test
    void toEntityUpdate_success() {
        UpdateTaskRequest request = new UpdateTaskRequest(
                "id1231",
                "user1@email.com1",
                "taskId1231",
                "Main Backend Task1",
                "description1",
                Priority.LOW,
                List.of("user1@email.com1", "user3@email.com1"),
                "user3@email.com1",
                LocalDateTime.now());

        Task taskStart = new Task(
                "taskId123",
                "Task Name",
                "Task Description",
                TaskStatus.CREATED,
                Priority.HIGH,
                "teamId123",
                List.of("email1@example.com", "email2@example.com"),
                "creator@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        Task task = TaskMapper.toEntityUpdate(request, taskStart);

        assertNotNull(task);
        assertEquals("user1@email.com1", request.getUserEmail());

        assertEquals("taskId123", task.getId());
        assertEquals("Main Backend Task1", task.getName());
        assertEquals("description1", task.getDescription());
        assertEquals(TaskStatus.CREATED, task.getStatus());
        assertEquals(Priority.LOW, task.getPriority());
        assertEquals("id1231", request.getTeamId());
        assertEquals(2, task.getCollaboratorsEmails().size());
        assertTrue(task.getCollaboratorsEmails().contains("user1@email.com1"));
        assertTrue(task.getCollaboratorsEmails().contains("user3@email.com1"));
        assertEquals("user3@email.com1", task.getCreatorEmail());
        assertNotNull(task.getStartsAt());
        assertNotNull(task.getFinishedAt());
        assertNotNull(task.getCreatedAt());
    }

    @Test
    void toEntityUpdate_null() {
        assertThrows(IllegalArgumentException.class, () -> {
            TaskMapper.toEntityUpdate(null, new Task());
        });
    }
}
