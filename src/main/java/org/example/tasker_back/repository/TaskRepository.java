package org.example.tasker_back.repository;

import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByTeamId(String teamId);

    @Query("{ 'teamId': ?0, 'status': ?1 }")
    List<Task> findByTeamIdAndStatus(String teamId, TaskStatus status);

}
