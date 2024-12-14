package org.example.tasker_back.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.TaskStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "task")
@Data
@NoArgsConstructor
public class Task {
    @Id
    private String id;

    private String name;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private String teamId;
    private List<String> collaboratorsEmails; // users emails who do this task
    private String creatorEmail;
    private LocalDateTime startsAt;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}
