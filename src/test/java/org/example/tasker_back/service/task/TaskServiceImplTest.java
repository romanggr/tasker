package org.example.tasker_back.service.task;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void getTasksFromTeam_success() {
        TasksRequest request = new TasksRequest("1", "user1@email.com");
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                List.of(),
                new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")),
                "creator@gmail.com");
        List<Task> tasks = List.of(new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team123", List.of("user1@email.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null));


        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(taskRepository.findByTeamId(request.getTeamId())).thenReturn(tasks);

        List<Task> response = taskService.getTasksFromTeam(request);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("task1", response.get(0).getId());
        verify(teamRepository, times(1)).findById(request.getTeamId());
        verify(taskRepository, times(1)).findByTeamId(request.getTeamId());
    }

    @Test
    void getTasksFromTeam_invalidTeamId() {
        TasksRequest request = new TasksRequest("1", "user1@email.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.getTasksFromTeam(request));
        verify(teamRepository, times(1)).findById(request.getTeamId());
    }


    @Test
    void getTasksFromTeam_userNotInTeam() {
        TasksRequest request = new TasksRequest("1", "user1@email.com");
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                List.of(),
                new ArrayList<>(List.of("creator@gmail.com")),
                "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));

        assertThrows(IllegalArgumentException.class, () -> taskService.getTasksFromTeam(request));
        verify(teamRepository, times(1)).findById(request.getTeamId());
    }


    @Test
    void getTasksFromTeamByStatus_success() {
        TasksByStatusRequest request = new TasksByStatusRequest("1", "user1@email.com", TaskStatus.RUNNING);
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                List.of(),
                new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")),
                "creator@gmail.com");
        List<Task> tasks = List.of(new Task("task1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "team123", List.of("user1@email.com"), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null));


        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(taskRepository.findByTeamIdAndStatus(request.getTeamId(), request.getStatus())).thenReturn(tasks);

        List<Task> response = taskService.getTasksFromTeamByStatus(request);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("task1", response.get(0).getId());
        verify(teamRepository, times(1)).findById(request.getTeamId());
        verify(taskRepository, times(1)).findByTeamIdAndStatus(request.getTeamId(), request.getStatus());
    }


    @Test
    void getTasksFromTeamByStatus_invalidTeamId() {
        TasksByStatusRequest request = new TasksByStatusRequest("1", "user1@email.com", TaskStatus.RUNNING);

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.getTasksFromTeamByStatus(request));
        verify(teamRepository, times(1)).findById(request.getTeamId());
    }

    @Test
    void getTasksFromTeamByStatus_userNotInTeam() {
        TasksByStatusRequest request = new TasksByStatusRequest("1", "user1@email.com", TaskStatus.RUNNING);
        Team dbTeam = new Team(
                "team123",
                "Original Team",
                List.of(),
                new ArrayList<>(List.of("creator@gmail.com")),
                "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));

        assertThrows(IllegalArgumentException.class, () -> taskService.getTasksFromTeamByStatus(request));
        verify(teamRepository, times(1)).findById(request.getTeamId());
    }


    @Test
    void joinToTask_success() {
        JoinToTaskRequest request = new JoinToTaskRequest("user1@email.com", "1", "1");
        User user = new User("123", "user1@email.com", "User 1", "ssrgsergsdfg23", new ArrayList<>(List.of(Role.DESIGNER, Role.BIG_DATA)), new ArrayList<>(List.of("1")), new ArrayList<>(List.of("1")));
        Task task = new Task("1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "1", new ArrayList<>(List.of("user1@gmail.com", "user2@gmail.com")), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null);
        Team dbTeam = new Team("team123", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByEmail(request.getUserEmail())).thenReturn(Optional.of(user));
        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(task));

        Task response = taskService.joinToTask(request);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertTrue(response.getCollaboratorsEmails().contains("user1@email.com"));
        assertTrue(user.getTaskIds().contains("1"));

        verify(userRepository, times(1)).findByEmail(request.getUserEmail());
        verify(userRepository, times(1)).save(any());
        verify(taskRepository, times(1)).findById(request.getTaskId());
        verify(taskRepository, times(1)).save(any());
        verify(teamRepository, times(1)).findById(request.getTeamId());

    }

    @Test
    void joinToTask_invalidTeamId() {
        JoinToTaskRequest request = new JoinToTaskRequest("user1@email.com", "1", "1");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.joinToTask(request));
        verify(teamRepository, times(1)).findById(request.getTeamId());
    }

    @Test
    void joinToTask_userNotInTeam() {
        JoinToTaskRequest request = new JoinToTaskRequest("user1@email.com", "1", "1");
        Team dbTeam = new Team("team123", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));

        assertThrows(IllegalArgumentException.class, () -> taskService.joinToTask(request));
        verify(teamRepository, times(1)).findById(request.getTeamId());
    }

    @Test
    void joinToTask_invalidUserId() {
        JoinToTaskRequest request = new JoinToTaskRequest("user1@email.com", "1", "1");
        Team dbTeam = new Team("team123", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByEmail(request.getUserEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.joinToTask(request));

        verify(userRepository, times(1)).findByEmail(request.getUserEmail());
        verify(teamRepository, times(1)).findById(request.getTeamId());
    }


    @Test
    void joinToTask_invalidTaskId() {
        JoinToTaskRequest request = new JoinToTaskRequest("user1@email.com", "1", "1");
        User user = new User("123", "user1@email.com", "User 1", "ssrgsergsdfg23", new ArrayList<>(List.of(Role.DESIGNER, Role.BIG_DATA)), new ArrayList<>(List.of("1")), new ArrayList<>(List.of("1")));
        Team dbTeam = new Team("team123", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(userRepository.findByEmail(request.getUserEmail())).thenReturn(Optional.of(user));
        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.joinToTask(request));

        verify(userRepository, times(1)).findByEmail(request.getUserEmail());
        verify(userRepository, times(1)).save(any());
        verify(taskRepository, times(1)).findById(request.getTaskId());
        verify(teamRepository, times(1)).findById(request.getTeamId());
    }


    @Test
    void createTask_success() {
        CreateTaskRequest request = new CreateTaskRequest("1", "Implement Feature X", "Develop and integrate feature X into the system", Priority.HIGH, new ArrayList<>(List.of("user2@gmail.com")), "user1@gmail.com", LocalDateTime.of(2025, 1, 15, 10, 0));
        Team dbTeam = new Team("1", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@gmail.com")), "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(taskRepository.save(any())).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId("1");
            return task;
        });

        Task response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals(2, response.getCollaboratorsEmails().size());
        verify(teamRepository, times(1)).findById(request.getTeamId());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void createTask_invalidTeamId() {
        CreateTaskRequest request = new CreateTaskRequest("1", "Implement Feature X", "Develop and integrate feature X into the system", Priority.HIGH, new ArrayList<>(List.of("user2@gmail.com")), "user1@gmail.com", LocalDateTime.of(2025, 1, 15, 10, 0));
        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(request));

        verify(teamRepository, times(1)).findById(request.getTeamId());
    }

    @Test
    void createTask_invalidUserId() {
        CreateTaskRequest request = new CreateTaskRequest("1", "Implement Feature X", "Develop and integrate feature X into the system", Priority.HIGH, new ArrayList<>(List.of("user2@gmail.com")), "user1@gmail.com", LocalDateTime.of(2025, 1, 15, 10, 0));
        Team dbTeam = new Team("1", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com")), "creator@gmail.com");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));

        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(request));

        verify(teamRepository, times(1)).findById(request.getTeamId());
    }


    @Test
    void finishTask_success() {
        Team dbTeam = new Team("2", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");
        Task task = new Task("1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "2", new ArrayList<>(List.of("user1@gmail.com", "user2@gmail.com")), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null);
        FinishTaskRequest request = new FinishTaskRequest("1", "user1@email.com", "2");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task response = taskService.finishTask(request);

        assertNotNull(response);
        assertEquals(response.getStatus(), TaskStatus.FINISHED);
        assertNotNull(response.getFinishedAt());

        verify(teamRepository, times(1)).findById(request.getTeamId());
        verify(taskRepository, times(1)).findById(request.getTaskId());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void finishTask_alreadyFinished() {
        Team dbTeam = new Team("2", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");
        Task task = new Task("1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.FINISHED, Priority.HIGH, "2", new ArrayList<>(List.of("user1@gmail.com", "user2@gmail.com")), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), LocalDateTime.now());
        FinishTaskRequest request = new FinishTaskRequest("1", "user1@email.com", "2");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(task));

        assertThrows(RuntimeException.class, () -> taskService.finishTask(request));


        verify(teamRepository, times(1)).findById(request.getTeamId());
        verify(taskRepository, times(1)).findById(request.getTaskId());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void finishTask_invalidTaskId() {
        Team dbTeam = new Team("2", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");
        FinishTaskRequest request = new FinishTaskRequest("1", "user1@email.com", "2");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));
        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.finishTask(request));


        verify(teamRepository, times(1)).findById(request.getTeamId());
        verify(taskRepository, times(1)).findById(request.getTaskId());
    }

    @Test
    void finishTask_invalidTeamId() {
        FinishTaskRequest request = new FinishTaskRequest("1", "user1@email.com", "2");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.finishTask(request));

        verify(teamRepository, times(1)).findById(request.getTeamId());
    }

    @Test
    void finishTask_userNotInTeam() {
        Team dbTeam = new Team("2", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");
        FinishTaskRequest request = new FinishTaskRequest("1", "user111@email.com", "2");

        when(teamRepository.findById(request.getTeamId())).thenReturn(Optional.of(dbTeam));

        assertThrows(IllegalArgumentException.class, () -> taskService.finishTask(request));

        verify(teamRepository, times(1)).findById(request.getTeamId());
    }


    @Test
    void updateTask_success() {
        Task task = new Task("1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "2", new ArrayList<>(List.of("user1@email.com", "user2@gmail.com")), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null);
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest("2", "user1@email.com", "1", "Develop and integrate feature Y into the system", Priority.LOW, LocalDateTime.of(2025, 1, 15, 10, 0));

        when(taskRepository.findById(updateTaskRequest.getTaskId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task response = taskService.updateTask(updateTaskRequest);

        assertNotNull(response);
        assertEquals(response.getPriority(), updateTaskRequest.getPriority());
        assertEquals(response.getDescription(), updateTaskRequest.getDescription());
        assertEquals(response.getStartsAt(), updateTaskRequest.getStartsAt());

        verify(taskRepository, times(1)).findById(updateTaskRequest.getTaskId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_invalidTaskId() {
       UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest("2", "user1@email.com", "1", "Develop and integrate feature Y into the system", Priority.LOW, LocalDateTime.of(2025, 1, 15, 10, 0));

        when(taskRepository.findById(updateTaskRequest.getTaskId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(updateTaskRequest));

        verify(taskRepository, times(1)).findById(updateTaskRequest.getTaskId());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_userNotCollaborator(){
        Task task = new Task("1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "2", new ArrayList<>(List.of("user1@email.com", "user2@gmail.com")), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null);
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest("2", "user1123@email.com", "1", "Develop and integrate feature Y into the system", Priority.LOW, LocalDateTime.of(2025, 1, 15, 10, 0));

        when(taskRepository.findById(updateTaskRequest.getTaskId())).thenReturn(Optional.of(task));

        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(updateTaskRequest));

        verify(taskRepository, times(1)).findById(updateTaskRequest.getTaskId());
        verify(taskRepository, never()).save(any(Task.class));
    }



    @Test
    void getTaskCollaborators_success(){
        String taskId = "1";
        Task task = new Task("1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "2", new ArrayList<>(List.of("user1@email.com", "user2@gmail.com", "creator@gmail.com")), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        List<String> response = taskService.getTaskCollaborators(taskId);

        assertNotNull(response);
        assertEquals(response.size(), 3);

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getTaskCollaborators_invalidTaskId(){
        String taskId = "1";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskCollaborators(taskId));

        verify(taskRepository, times(1)).findById(taskId);

    }
}