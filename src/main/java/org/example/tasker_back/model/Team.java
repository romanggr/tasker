package org.example.tasker_back.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "team")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    private String id;

    private String name;
    private List<Task> tasks;
    private List<String> collaboratorsEmails;
    private String creatorEmail;
}

