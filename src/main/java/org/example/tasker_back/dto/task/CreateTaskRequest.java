package org.example.tasker_back.dto.task;

import lombok.Data;
import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateTaskRequest {
    private String teamId;
    private String name;
    private String description;
    private Priority priority;
    private List<String> collaboratorsEmails;
    private String creatorEmail;
    private LocalDateTime startsAt;
}
