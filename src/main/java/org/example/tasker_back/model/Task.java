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


//User user = new User("123", "user1@email.com", "User 1", "ssrgsergsdfg23", new ArrayList<>(List.of(Role.DESIGNER, Role.BIG_DATA)), new ArrayList<>(List.of("1")), new ArrayList<>(List.of("1")));
//Task task = new Task("1", "Implement Authentication", "Design and implement user authentication using Spring Security", TaskStatus.RUNNING, Priority.HIGH, "1", new ArrayList<>(List.of("user1@gmail.com", "user2@gmail.com")), "creator@gmail.com", LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2024, 12, 31, 15, 0), null);
//Team dbTeam = new Team("team123", "Original Team", List.of(), new ArrayList<>(List.of("creator@gmail.com", "user1@email.com")), "creator@gmail.com");
