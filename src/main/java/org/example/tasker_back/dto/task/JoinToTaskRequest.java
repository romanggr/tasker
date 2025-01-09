package org.example.tasker_back.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JoinToTaskRequest {
    private String userEmail;
    private String taskId;
    private String teamId;
}
