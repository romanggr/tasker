package org.example.tasker_back.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FinishTaskRequest {
    private String taskId;
    private String userEmail;
    private String teamId;

}
