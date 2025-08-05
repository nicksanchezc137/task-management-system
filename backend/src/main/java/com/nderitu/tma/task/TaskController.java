package com.nderitu.tma.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.nderitu.tma.user.User;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAuthority('task:create')")
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        TaskResponse task = taskService.createTask(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('task:read:all') or (hasAuthority('task:read:own') and #assignee == authentication.principal.id)")
    public ResponseEntity<Page<TaskResponse>> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Integer assignee,
            Pageable pageable) {
        Page<TaskResponse> tasks = taskService.getTasks(status, assignee, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('task:read:all') or (hasAuthority('task:read:own') and @taskService.isTaskAssignedToUser(#taskId, authentication.principal.id))")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long taskId) {
        TaskResponse task = taskService.getTask(taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAuthority('task:update:all') or (hasAuthority('task:update:own') and @taskService.isTaskAssignedToUser(#taskId, authentication.principal.id))")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        TaskResponse task = taskService.updateTask(taskId, request, currentUser);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAuthority('task:delete:all')")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/assign")
    @PreAuthorize("hasAuthority('task:assign')")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable Long taskId,
            @RequestParam Integer assigneeId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        TaskResponse task = taskService.assignTask(taskId, assigneeId, currentUser);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasAuthority('task:update:all') or (hasAuthority('task:update:own') and @taskService.isTaskAssignedToUser(#taskId, authentication.principal.id))")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus status,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        TaskResponse task = taskService.updateTaskStatus(taskId, status, currentUser);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/my-tasks")
    @PreAuthorize("hasAuthority('task:read:own')")
    public ResponseEntity<List<TaskResponse>> getMyTasks(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<TaskResponse> tasks = taskService.getTasksByAssignee(currentUser);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/created-by-me")
    @PreAuthorize("hasAuthority('task:read:own')")
    public ResponseEntity<List<TaskResponse>> getTasksCreatedByMe(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<TaskResponse> tasks = taskService.getTasksByCreator(currentUser);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('task:read:all')")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<TaskResponse> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }
} 