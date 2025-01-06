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

    @Indexed
    private String email;

    private String fullName;
    private String password;
    private List<Role> roles;
    private List<String> taskIds;
    private List<String> teamIds;
}


















