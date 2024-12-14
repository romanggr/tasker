package org.example.tasker_back.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tasker_back.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "users")
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





// team (if creator)
// create team
// update team
// leave team
// add users to team
// delete users from team
// get updated tasks from team (reload page)


// get all tasks
// get all tass where you are member
// join to task
// create task
// finish task













