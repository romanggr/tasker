package org.example.tasker_back.model;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Task {
    @Id
    private String id;

    private String name;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private String teamId;
    private List<String> collaboratorsEmails;
    private String creatorEmail;
    private LocalDateTime startsAt;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}


//new Task(
//    "task1",
//            "Implement Authentication",
//            "Design and implement user authentication using Spring Security",
//    TaskStatus.IN_PROGRESS,
//    Priority.HIGH,
//    "team123",
//    List.of("user1@gmail.com", "user2@gmail.com"),
//    "creator@gmail.com",
//            LocalDateTime.of(2025, 1, 1, 9, 0),
//    LocalDateTime.of(2024, 12, 31, 15, 0),
//    null
//            )