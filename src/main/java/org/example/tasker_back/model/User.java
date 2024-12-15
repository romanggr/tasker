package org.example.tasker_back.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tasker_back.enums.Priority;
import org.example.tasker_back.enums.Role;
import org.example.tasker_back.enums.TaskStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "users")
@AllArgsConstructor
public class User {
    @Id
    private String id;

    private String email;
    private String fullName;
    private String password;
    private List<Role> roles;
    private List<String> taskIds;
    private List<String> teamIds;
}


//Task task1 = new Task(
//        "1",
//        "Test Task",
//        "This is a test task for the project",
//        TaskStatus.RUNNING,
//        Priority.HIGH,
//        "team-123", // teamId
//        Arrays.asList("user1@example.com", "user2@example.com"),
//        "user1@example.com",
//        LocalDateTime.now().plusDays(1),
//        LocalDateTime.now(),
//        LocalDateTime.now().plusDays(2)
//)















