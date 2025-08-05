package com.nderitu.tma.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nderitu.tma.user.User;
import com.nderitu.tma.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(TaskRequest request, User creator) {
        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .assignee(assignee)
                .creator(creator)
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromTask(savedTask);
    }

    public TaskResponse updateTask(Long taskId, TaskRequest request, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check if user can update the task (creator or assignee)
        if (!task.getCreator().getId().equals(currentUser.getId()) && 
            (task.getAssignee() == null || !task.getAssignee().getId().equals(currentUser.getId()))) {
            throw new RuntimeException("You can only update tasks you created or are assigned to");
        }

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setAssignee(assignee);

        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromTask(updatedTask);
    }

    public void deleteTask(Long taskId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

      

        taskRepository.delete(task);
    }

    public TaskResponse getTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return TaskResponse.fromTask(task);
    }

    public Page<TaskResponse> getTasks(TaskStatus status, Integer assigneeId, Pageable pageable) {
        User assignee = null;
        if (assigneeId != null) {
            assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Page<Task> tasks = taskRepository.findByStatusAndAssignee(status, assignee, pageable);
        return tasks.map(TaskResponse::fromTask);
    }

    public List<TaskResponse> getTasksByAssignee(User assignee) {
        List<Task> tasks = taskRepository.findByAssignee(assignee);
        return tasks.stream()
                .map(TaskResponse::fromTask)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByCreator(User creator) {
        List<Task> tasks = taskRepository.findByCreator(creator);
        return tasks.stream()
                .map(TaskResponse::fromTask)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        return tasks.stream()
                .map(TaskResponse::fromTask)
                .collect(Collectors.toList());
    }

    public TaskResponse assignTask(Long taskId, Integer assigneeId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Only creator can assign the task
        if (!task.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only assign tasks you created");
        }

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        task.setAssignee(assignee);
        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromTask(updatedTask);
    }

    public TaskResponse updateTaskStatus(Long taskId, TaskStatus newStatus, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check if user can update the task status (creator or assignee)
        if (!task.getCreator().getId().equals(currentUser.getId()) && 
            (task.getAssignee() == null || !task.getAssignee().getId().equals(currentUser.getId()))) {
            throw new RuntimeException("You can only update status of tasks you created or are assigned to");
        }

        // Validate status transition
        if (!isValidStatusTransition(task.getStatus(), newStatus)) {
            throw new RuntimeException("Invalid status transition from " + task.getStatus() + " to " + newStatus);
        }

        task.setStatus(newStatus);
        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromTask(updatedTask);
    }

    private boolean isValidStatusTransition(TaskStatus currentStatus, TaskStatus newStatus) {
        if (currentStatus == TaskStatus.TODO) {
            return newStatus == TaskStatus.IN_PROGRESS || newStatus == TaskStatus.DONE;
        } else if (currentStatus == TaskStatus.IN_PROGRESS) {
            return newStatus == TaskStatus.DONE;
        } else if (currentStatus == TaskStatus.DONE) {
            return false; // Cannot transition from DONE
        }
        return false;
    }

    public boolean isTaskAssignedToUser(Long taskId, Integer userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        return task.getAssignee() != null && task.getAssignee().getId().equals(userId);
    }
} 