package org.example.tasker_back.utils;

import org.example.tasker_back.dto.task.CreateTaskRequest;
import org.example.tasker_back.dto.task.UpdateTaskRequest;
import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.model.Task;
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
        assertThrows(IllegalArgumentException.class, () -> TaskMapper.toEntityCreate(null));
    }



    @Test
    void toEntityCreate_emptyName() {
        CreateTaskRequest request = new CreateTaskRequest("id123", "",
                "description", Priority.HIGH, List.of("user1@email.com", "user3@email.com"),
                "user3@email.com", LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> TaskMapper.toEntityCreate(request));
    }

    @Test
    void toEntityCreate_emptyCreatorEmail() {
        CreateTaskRequest request = new CreateTaskRequest("id123", "Main",
                "description", Priority.HIGH, List.of("user1@email.com", "user3@email.com"),
                "", LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> TaskMapper.toEntityCreate(request));
    }

    @Test
    void toEntityCreate_emptyTeamId() {
        CreateTaskRequest request = new CreateTaskRequest("", "Main",
                "description", Priority.HIGH, List.of("user1@email.com", "user3@email.com"),
                "user3@email.com", LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> TaskMapper.toEntityCreate(request));
    }

    @Test
    void toEntityCreate_emptyPriority() {
        CreateTaskRequest request = new CreateTaskRequest("123", "Main",
                "description", null, List.of("user1@email.com", "user3@email.com"),
                "user3@email.com", LocalDateTime.now());

        Priority priority = TaskMapper.toEntityCreate(request).getPriority();

        assertEquals(Priority.LOW, priority);
    }



    @Test
    void toEntityUpdate_success() {
        Task existingTask = new Task(
                "task1",
                "Original Task",
                "Original Description",
                TaskStatus.RUNNING,
                Priority.MEDIUM,
                "team123",
                List.of("user1@example.com"),
                "creator@example.com",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2024, 12, 31, 15, 0),
                null
        );

        UpdateTaskRequest request = new UpdateTaskRequest(
                "team123",
                "user1@example.com",
                "task1",
                "Updated Description",
                Priority.HIGH,
                LocalDateTime.of(2025, 1, 2, 10, 0)
        );

        Task updatedTask = TaskMapper.toEntityUpdate(request, existingTask);

        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals(Priority.HIGH, updatedTask.getPriority());
        assertEquals(LocalDateTime.of(2025, 1, 2, 10, 0), updatedTask.getStartsAt());
    }

    @Test
    void toEntityUpdate_nullRequest() {
        Task existingTask = new Task();
        UpdateTaskRequest request = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TaskMapper.toEntityUpdate(request, existingTask));
        assertEquals("Request is null", exception.getMessage());
    }


    @Test
    void toEntityUpdate_emptyDescription() {
        Task existingTask = new Task(
                "task1",
                "Original Task",
                "Original Description",
                TaskStatus.RUNNING,
                Priority.MEDIUM,
                "team123",
                List.of("user1@example.com"),
                "creator@example.com",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2024, 12, 31, 15, 0),
                null
        );

        UpdateTaskRequest request = new UpdateTaskRequest(
                "team123",
                "user1@example.com",
                "task1",
                "",
                Priority.HIGH,
                LocalDateTime.of(2025, 1, 2, 10, 0)
        );

        assertThrows(IllegalArgumentException.class, () -> TaskMapper.toEntityUpdate(request, existingTask));
    }

    @Test
    void toEntityUpdate_emptyPriority() {
        Task existingTask = new Task(
                "task1",
                "Original Task",
                "Original Description",
                TaskStatus.RUNNING,
                Priority.MEDIUM,
                "team123",
                List.of("user1@example.com"),
                "creator@example.com",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2024, 12, 31, 15, 0),
                null
        );

        UpdateTaskRequest request = new UpdateTaskRequest(
                "team123",
                "user1@example.com",
                "task1",
                "A description",
                null,
                LocalDateTime.of(2025, 1, 2, 10, 0)
        );

        assertThrows(IllegalArgumentException.class, () -> TaskMapper.toEntityUpdate(request, existingTask));
    }

    @Test
    void toEntityUpdate_emptyStartsAt() {
        Task existingTask = new Task(
                "task1",
                "Original Task",
                "Original Description",
                TaskStatus.RUNNING,
                Priority.MEDIUM,
                "team123",
                List.of("user1@example.com"),
                "creator@example.com",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2024, 12, 31, 15, 0),
                null
        );

        UpdateTaskRequest request = new UpdateTaskRequest(
                "team123",
                "user1@example.com",
                "task1",
                "A description",
                Priority.HIGH,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> TaskMapper.toEntityUpdate(request, existingTask));
    }
}
