package org.example.tasker_back.dto.task;

import lombok.Data;

@Data
public class JoinToTaskRequest {
    private String userEmail;
    private String taskId;
    private String teamId;
}
