package com.nderitu.tma.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UserResponse assignee;
    private UserResponse creator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse {
        private Integer id;
        private String username;
        private String email;
    }

    public static TaskResponse fromTask(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assignee(task.getAssignee() != null ? UserResponse.builder()
                        .id(task.getAssignee().getId())
                        .username(task.getAssignee().getUsername())
                        .email(task.getAssignee().getEmail())
                        .build() : null)
                .creator(UserResponse.builder()
                        .id(task.getCreator().getId())
                        .username(task.getCreator().getUsername())
                        .email(task.getCreator().getEmail())
                        .build())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
} 