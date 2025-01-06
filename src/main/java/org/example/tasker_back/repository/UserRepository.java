package org.example.tasker_back.repository;

import org.example.tasker_back.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    List<User> findByEmailIn(List<String> emails);

    List<User> findByTeamIdsContaining(String teamId);

}
