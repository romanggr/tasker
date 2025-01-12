package org.example.tasker_back.service.user.user;


import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.user.AuthResponse;
import org.example.tasker_back.dto.user.UpdatePasswordRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;
import org.example.tasker_back.model.Task;
import org.example.tasker_back.model.Team;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.TaskRepository;
import org.example.tasker_back.repository.TeamRepository;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.security.JwtService;
import org.example.tasker_back.utils.UserMapper;
import org.example.tasker_back.utils.UserValidation;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;
    private final JwtService jwtService;


    @Override
    public AuthResponse updatePassword(UpdatePasswordRequest request) {
        User userDb = userRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserValidation.isValidUpdatingPassword(request, userDb.getPassword());

        userDb.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        userRepository.save(userDb);

        return AuthResponse.builder()
                .token(jwtService.generateToken(userDb.getEmail()))
                .userDto(UserMapper.toDTO(userDb))
                .build();
    }


    @Transactional
    @Override
    public AuthResponse updateUser(UpdateUserRequest request) {
        User userDb = userRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        updateUserInTeam(userDb.getEmail(), request.getEmail(), userDb.getTeamIds());
        updateUserInTask(userDb.getEmail(), request.getEmail(), userDb.getTaskIds());

        UserValidation.isValidUpdatingUser(request, userDb.getEmail(), userRepository);

        User updatedUser = userRepository.save(UserMapper.updateUser(request, userDb));
        return AuthResponse.builder()
                .token(jwtService.generateToken(updatedUser.getEmail()))
                .userDto(UserMapper.toDTO(updatedUser))
                .build();
    }


    private void updateUserInTeam(String oldEmail, String newEmail, List<String> teamsIds) {
        if (teamsIds == null || teamsIds.isEmpty()) return;
        List<Team> teams = teamRepository.findAllById(teamsIds);

        teams.forEach(team -> {
            List<String> updatedCollaborators = team.getCollaboratorsEmails().stream()
                    .map(email -> email.equals(oldEmail) ? newEmail : email)
                    .collect(Collectors.toList());
            System.out.println(updatedCollaborators);
            team.setCollaboratorsEmails(updatedCollaborators);

            if (Objects.equals(team.getCreatorEmail(), oldEmail)) {
                team.setCreatorEmail(newEmail);
            }
        });

        teamRepository.saveAll(teams);
    }

    private void updateUserInTask(String oldEmail, String newEmail, List<String> tasksIds) {
        if (tasksIds == null || tasksIds.isEmpty()) return;
        List<Task> tasks = taskRepository.findAllById(tasksIds);

        tasks.forEach(task -> {
            List<String> updatedCollaborators = task.getCollaboratorsEmails().stream()
                    .map(email -> email.equals(oldEmail) ? newEmail : email)
                    .collect(Collectors.toList());
            task.setCollaboratorsEmails(updatedCollaborators);

            if (Objects.equals(task.getCreatorEmail(), oldEmail)) {
                task.setCreatorEmail(newEmail);
            }
        });

        taskRepository.saveAll(tasks);
    }
}
