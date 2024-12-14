package org.example.tasker_back.dto.task;

import lombok.Data;
import org.example.tasker_back.enums.TaskStatus;

@Data
public class TasksByStatusRequest {
    private String teamId;
    private String userEmail;
    private TaskStatus status;
}
