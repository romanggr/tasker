package org.example.tasker_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.task.*;
import org.example.tasker_back.model.Task;
import org.example.tasker_back.service.task.TaskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;

    @GetMapping("/teamTasks/{teamId}")
    public ResponseEntity<List<Task>> getTasksFromTeam(@RequestBody TasksRequest request) {
        List<Task> tasks = taskService.getTasksFromTeam(request);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/team/{teamId}/status")
    public ResponseEntity<List<Task>> getTasksFromTeamByStatus(@RequestBody TasksByStatusRequest request) {
        List<Task> tasks = taskService.getTasksFromTeamByStatus(request);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/join")
    public ResponseEntity<Task> joinToTask(@RequestBody JoinToTaskRequest request) {
        Task task = taskService.joinToTask(request);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/finish")
    public ResponseEntity<Task> finishTask(@RequestBody FinishTaskRequest request) {
        Task task = taskService.finishTask(request);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody UpdateTaskRequest request) {
        Task task = taskService.updateTask(request);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/collaborators/{taskId}")
    public ResponseEntity<List<String>> getTaskCollaborators(@PathVariable String taskId) {
        List<String> collaborators = taskService.getTaskCollaborators(taskId);
        return ResponseEntity.ok(collaborators);
    }
}