package org.example.tasker_back.repository;

import org.example.tasker_back.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> findByCollaboratorsEmailsContaining(String userEmail);

}
