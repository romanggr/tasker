package org.example.tasker_back.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.tasker_back.enums.Priority;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class UpdateTaskRequest {
    private String teamId;
    private String userEmail;

    private String taskId;
    private String description;
    private Priority priority;
    private LocalDateTime startsAt;
}
