package org.example.tasker_back.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TasksRequest {
    private String teamId;
    private String userEmail;
}
