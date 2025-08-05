package com.nderitu.tma.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    // Admin permissions - can CRUD all tasks and assign tasks
    TASK_READ_ALL("task:read:all"),
    TASK_UPDATE_ALL("task:update:all"),
    TASK_CREATE_ALL("task:create:all"),
    TASK_DELETE_ALL("task:delete:all"),
    TASK_ASSIGN("task:assign"),
    
    // User permissions - can only manage own tasks
    TASK_READ_OWN("task:read:own"),
    TASK_UPDATE_OWN("task:update:own"),
    TASK_CREATE("task:create"),
    
    // User management permissions
    USER_READ_ALL("user:read:all")

    ;

    @Getter
    private final String permission;
}
