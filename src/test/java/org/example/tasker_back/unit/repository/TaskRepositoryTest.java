package org.example.tasker_back.unit.repository;

import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.model.Task;
import org.example.tasker_back.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryTest {
    @Mock
    private TaskRepository taskRepository;

    @Test
    void findByTeamIdAndStatus_success() {
        Task task1 = new Task("task1", "Task 1", "Description 1", TaskStatus.RUNNING, Priority.HIGH, "team1",
                List.of("user1@gmail.com"), "creator@gmail.com", null, null, null);
        Task task2 = new Task("task2", "Task 2", "Description 2", TaskStatus.RUNNING, Priority.LOW, "team1",
                List.of("user2@gmail.com"), "creator@gmail.com", null, null, null);

        when(taskRepository.findByTeamIdAndStatus("team1", TaskStatus.RUNNING)).thenReturn(List.of(task1, task2));

        List<Task> result = taskRepository.findByTeamIdAndStatus("team1", TaskStatus.RUNNING);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findByTeamIdAndStatus("team1", TaskStatus.RUNNING);
    }

    @Test
    void findByCollaboratorsEmail_success() {
        Task task1 = new Task("task1", "Task 1", "Description 1", TaskStatus.CREATED, Priority.HIGH, "team1",
                List.of("user1@gmail.com"), "creator@gmail.com", null, null, null);
        Task task2 = new Task("task2", "Task 2", "Description 2", TaskStatus.RUNNING, Priority.LOW, "team1",
                List.of("user1@gmail.com"), "creator@gmail.com", null, null, null);

        when(taskRepository.findByCollaboratorsEmail("user1@gmail.com")).thenReturn(List.of(task1, task2));

        List<Task> result = taskRepository.findByCollaboratorsEmail("user1@gmail.com");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(task -> task.getCollaboratorsEmails().contains("user1@gmail.com")));
        verify(taskRepository, times(1)).findByCollaboratorsEmail("user1@gmail.com");
    }
}