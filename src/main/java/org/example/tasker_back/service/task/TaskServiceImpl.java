package org.example.tasker_back.service.task;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.task.*;
import org.example.tasker_back.enums.TaskStatus;
import org.example.tasker_back.model.Task;
import org.example.tasker_back.model.Team;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.TaskRepository;
import org.example.tasker_back.repository.TeamRepository;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.utils.TaskMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;


    @Override
    public List<Task> getTasksFromTeam(TasksRequest request) {
        checkUserInTeam(request.getTeamId(), request.getUserEmail());

        return taskRepository.findByTeamId(request.getTeamId());
    }

    @Override
    public List<Task> getTasksFromTeamByStatus(TasksByStatusRequest request) {
        checkUserInTeam(request.getTeamId(), request.getUserEmail());
        return taskRepository.findByTeamIdAndStatus(request.getTeamId(), request.getStatus());
    }

    @Transactional
    @Override
    public Task joinToTask(JoinToTaskRequest request) {
        checkUserInTeam(request.getTeamId(), request.getUserEmail());

        User userToUpdate = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("User with this email not found"));
        List<String> updatedTaskIds = userToUpdate.getTaskIds();
        updatedTaskIds.add(request.getTaskId());
        userToUpdate.setTaskIds(updatedTaskIds);
        userRepository.save(userToUpdate);

        Task taskToUpdate = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task with this id not found"));
        List<String> updatedCollaboratorsEmails = taskToUpdate.getCollaboratorsEmails();
        updatedCollaboratorsEmails.add(request.getUserEmail());
        taskToUpdate.setCollaboratorsEmails(updatedCollaboratorsEmails);
        taskRepository.save(taskToUpdate);

        return taskToUpdate;
    }


    @Transactional
    @Override
    public Task createTask(CreateTaskRequest request) {
        checkUserInTeam(request.getTeamId(), request.getCreatorEmail());

        Team team = teamRepository.findById(request.getTeamId()).orElseThrow(() -> new IllegalArgumentException("Team with this id not found"));

        if (!request.getCollaboratorsEmails().contains(request.getCreatorEmail())) {
            request.getCollaboratorsEmails().add(request.getCreatorEmail());
        }

        Task taskEntity = TaskMapper.toEntityCreate(request);
        Task taskDb = taskRepository.save(taskEntity);

        List<String> verifiedUserEmails = addInUser(taskDb); // This part adds a taskId to Users and checks whether users are in the task team. Those who are not in the team are removed
        taskDb.setCollaboratorsEmails(verifiedUserEmails);

        addInTeam(taskDb, team); // this part add taskId to Team

        return taskRepository.save(taskDb);
    }

    private void addInTeam(Task task, Team team) {
        team.getTasks().add(task);
        teamRepository.save(team);
    }

    private List<String> addInUser(Task task) {
        List<String> verifiedUserEmails = new ArrayList<>();
        List<User> usersToSave = new ArrayList<>();
        List<User> users = userRepository.findByEmailIn(task.getCollaboratorsEmails());

        for (User user : users) {
            if (user.getTeamIds().contains(task.getTeamId())) {
                user.getTaskIds().add(task.getId());
                verifiedUserEmails.add(user.getEmail());
                usersToSave.add(user);
            }
        }

        userRepository.saveAll(usersToSave);
        return verifiedUserEmails;
    }

    @Override
    public Task finishTask(FinishTaskRequest request) {
        checkUserInTeam(request.getTeamId(), request.getUserEmail());

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task with this id not found"));

        if (task.getStatus() == TaskStatus.FINISHED || task.getFinishedAt() != null)
            throw new RuntimeException("Task is already finished");

        task.setFinishedAt(LocalDateTime.now());
        task.setStatus(TaskStatus.FINISHED);

        return taskRepository.save(task);
    }


    @Override
    public Task updateTask(UpdateTaskRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task with this id not found"));

        if (!task.getCollaboratorsEmails().contains(request.getUserEmail()))
            throw new IllegalArgumentException("Only collaborators can update tasks");

        Task updatedTask = TaskMapper.toEntityUpdate(request, task);

        return taskRepository.save(updatedTask);
    }



    @Override
    public List<String> getTaskCollaborators(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with this id not found"));

        return task.getCollaboratorsEmails();
    }


    private void checkUserInTeam(String teamId, String email) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team with this id not found"));

        boolean isUserInTeam = team.getCollaboratorsEmails().contains(email);

        if (!isUserInTeam) {
            throw new IllegalArgumentException("User is not in team. Access denied");
        }
    }
}
