package org.example.tasker_back.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.tasker_back.enums.Priority;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class UpdateTaskRequest {
    private String teamId;
    private String userEmail;

    private String taskId;
    private String name;
    private String description;
    private Priority priority;
    private List<String> collaboratorsEmails;
    private String creatorEmail;
    private LocalDateTime startsAt;
}
