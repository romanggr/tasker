package org.example.tasker_back.utils;

import org.example.tasker_back.dto.task.CreateTaskRequest;
import org.example.tasker_back.dto.task.UpdateTaskRequest;
import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.model.Task;

import java.time.LocalDateTime;

public class TaskMapper {
    public static Task toEntity(CreateTaskRequest request){
        if(request == null) throw new IllegalArgumentException("Request is null");

        Task task = new Task();
        task.setTeamId(request.getTeamId());
        task.setDescription(request.getDescription());
        task.setName(request.getName());
        task.setPriority(request.getPriority());
        task.setStatus(TaskStatus.CREATED);
        task.setCollaboratorsEmails(request.getCollaboratorsEmails());
        task.setCreatorEmail(request.getCreatorEmail());
        task.setStartsAt(request.getStartsAt());
        task.setCreatedAt(LocalDateTime.now());

        return task;
    }

    public static Task toEntityUpdate(UpdateTaskRequest request, Task task){
        if(request == null) throw new IllegalArgumentException("Request is null");

        task.setDescription(request.getDescription());
        task.setName(request.getName());
        task.setPriority(request.getPriority());
        task.setCollaboratorsEmails(request.getCollaboratorsEmails());
        task.setCreatorEmail(request.getCreatorEmail());
        task.setStartsAt(request.getStartsAt());

        return task;
    }
}
