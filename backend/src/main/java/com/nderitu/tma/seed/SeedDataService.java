package com.nderitu.tma.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nderitu.tma.auth.AuthenticationService;
import com.nderitu.tma.auth.RegisterRequest;
import com.nderitu.tma.task.Task;
import com.nderitu.tma.task.TaskPriority;
import com.nderitu.tma.task.TaskRepository;
import com.nderitu.tma.task.TaskStatus;
import com.nderitu.tma.user.Role;
import com.nderitu.tma.user.User;
import com.nderitu.tma.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeedDataService {

    private final AuthenticationService authenticationService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public void loadSeedData() {
        try {
            log.info("Loading seed data from JSON file...");
            
           
            ClassPathResource resource = new ClassPathResource("seed-data.json");
            SeedData seedData = objectMapper.readValue(resource.getInputStream(), SeedData.class);
            
            
            Map<String, User> userMap = createUsers(seedData.getUsers());
            
            createTasks(seedData.getTasks(), userMap);
            
            log.info("Seed data loaded successfully!");
            
        } catch (IOException e) {
            log.error("Failed to load seed data:", e.getMessage());
            throw new RuntimeException("Failed to load seed data", e);
        }
    }

    private Map<String, User> createUsers(List<SeedUser> seedUsers) {
        Map<String, User> userMap = new HashMap<>();
        
        for (SeedUser seedUser : seedUsers) {
            try {
                if (userRepository.findByEmail(seedUser.getEmail()).isPresent()) {
                    log.info("User already exists, skipping...", seedUser.getEmail());
                    User existingUser = userRepository.findByEmail(seedUser.getEmail()).get();
                    userMap.put(seedUser.getEmail(), existingUser);
                    continue;
                }
               
                RegisterRequest registerRequest = RegisterRequest.builder()
                        .username(seedUser.getUsername())
                        .email(seedUser.getEmail())
                        .password(seedUser.getPassword())
                        .role(Role.valueOf(seedUser.getRole()))
                        .build();
                
                var response = authenticationService.register(registerRequest);
                User createdUser = userRepository.findByEmail(seedUser.getEmail()).orElseThrow();
                userMap.put(seedUser.getEmail(), createdUser);
                
                log.info("Created user: {} ({})", seedUser.getUsername(), seedUser.getEmail());
                
            } catch (Exception e) {
                log.error("Failed to create user {}: {}", seedUser.getEmail(), e.getMessage());
            }
        }
        
        return userMap;
    }

    private void createTasks(List<SeedTask> seedTasks, Map<String, User> userMap) {
        for (SeedTask seedTask : seedTasks) {
            try {
                User assignee = userMap.get(seedTask.getAssigneeEmail());
                User creator = userMap.get(seedTask.getCreatorEmail());
                
                if (assignee == null || creator == null) {
                    log.error("Cannot create task '{}': assignee or creator not found", seedTask.getTitle());
                    continue;
                }
                
                Task task = Task.builder()
                        .title(seedTask.getTitle())
                        .description(seedTask.getDescription())
                        .status(TaskStatus.valueOf(seedTask.getStatus()))
                        .priority(TaskPriority.valueOf(seedTask.getPriority()))
                        .assignee(assignee)
                        .creator(creator)
                        .build();
                
                taskRepository.save(task);
                log.info("Created task: {} (assigned to: {})", seedTask.getTitle(), assignee.getUsername());
                
            } catch (Exception e) {
                log.error("Failed to create task '{}': {}", seedTask.getTitle(), e.getMessage());
            }
        }
    }
} 