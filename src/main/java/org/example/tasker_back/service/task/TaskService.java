package org.example.tasker_back.service.task;

import org.example.tasker_back.dto.task.*;
import org.example.tasker_back.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTasksFromTeam(TasksRequest request);

    List<Task> getTasksFromTeamByStatus(TasksByStatusRequest request);

    Task joinToTask(JoinToTaskRequest request);

    Task createTask(CreateTaskRequest request);

    Task finishTask(FinishTaskRequest request);

    Task updateTask(UpdateTaskRequest request);

    List<String> getTaskCollaborators(String taskId);

}

