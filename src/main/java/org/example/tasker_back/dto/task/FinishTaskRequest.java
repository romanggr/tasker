package org.example.tasker_back.dto.task;

import lombok.Data;

@Data
public class FinishTaskRequest {
    private String taskId;
    private String userEmail;
    private String teamId;

}
