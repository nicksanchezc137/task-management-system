package com.nderitu.tma.seed;

import lombok.Data;

@Data
public class SeedTask {
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assigneeEmail;
    private String creatorEmail;
} 