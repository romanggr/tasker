package org.example.tasker_back.utils;

import org.example.tasker_back.dto.task.CreateTaskRequest;
import org.example.tasker_back.dto.task.UpdateTaskRequest;
import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.model.Task;

import java.time.LocalDateTime;

public class TaskMapper {
    public static Task toEntityCreate(CreateTaskRequest request) {
        if (request == null) throw new IllegalArgumentException("Request is null");
        if (request.getName() == null || request.getName().isEmpty())
            throw new IllegalArgumentException("Request name is null");
        if (request.getCreatorEmail() == null || request.getCreatorEmail().isEmpty())
            throw new IllegalArgumentException("Request creator name is null");
        if (request.getTeamId() == null || request.getTeamId().isEmpty())
            throw new IllegalArgumentException("Request team id is null");


        Task task = new Task();
        task.setTeamId(request.getTeamId());
        task.setDescription(request.getDescription());
        task.setName(request.getName());
        task.setPriority(request.getPriority() == null ? Priority.LOW : request.getPriority());
        task.setStatus(TaskStatus.CREATED);
        task.setCollaboratorsEmails(request.getCollaboratorsEmails());
        task.setCreatorEmail(request.getCreatorEmail());
        task.setStartsAt(request.getStartsAt() == null ? LocalDateTime.now() : request.getStartsAt());
        task.setCreatedAt(LocalDateTime.now());

        return task;
    }

    public static Task toEntityUpdate(UpdateTaskRequest request, Task task) {
        if (request == null) throw new IllegalArgumentException("Request is null");
        if (request.getDescription() == null || request.getDescription().isEmpty())
            throw new IllegalArgumentException("Request description is null");
        if (request.getPriority() == null) throw new IllegalArgumentException("Request priority is null");
        if (request.getStartsAt() == null) throw new IllegalArgumentException("Request start time is null");

        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority() == null ? task.getPriority() : request.getPriority());
        task.setStartsAt(request.getStartsAt() == null ? task.getStartsAt() : request.getStartsAt());

        return task;
    }
}
