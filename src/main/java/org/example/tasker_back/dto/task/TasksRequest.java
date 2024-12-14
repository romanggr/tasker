package org.example.tasker_back.dto.task;

import lombok.Data;

@Data
public class TasksRequest {
    private String teamId;
    private String userEmail;
}
