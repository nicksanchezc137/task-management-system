# Task Management API Documentation

## Overview
This document describes the Task Management API built with Spring Boot 3.x, Spring Security, and JWT authentication.

## Features
- **CRUD operations for tasks**
- **Task filtering by status and assignee**
- **Task assignment to users**
- **Status transitions (TODO → IN_PROGRESS → DONE)**
- **JWT-based authentication**
- **Role-based access control**
- **Global exception handling**
- **Bean validation**

## Data Models

### User
```java
User: id, firstname, lastname, email, password, role, createdAt
```

### Task
```java
Task: id, title, description, status, priority, assigneeId, creatorId, createdAt, updatedAt
```

### Task Status
- `TODO` - Task is pending
- `IN_PROGRESS` - Task is being worked on
- `DONE` - Task is completed

### Task Priority
- `LOW` - Low priority
- `MEDIUM` - Medium priority
- `HIGH` - High priority
- `URGENT` - Urgent priority

## API Endpoints

### Authentication Endpoints

#### Register User
```
POST /api/v1/auth/register
Content-Type: application/json

{
  "firstname": "John",
  "lastname": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "role": "USER"
}
```

#### Login
```
POST /api/v1/auth/authenticate
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

### Task Management Endpoints

All task endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

#### Create Task
```
POST /api/v1/tasks
Content-Type: application/json

{
  "title": "Implement User Authentication",
  "description": "Create a secure authentication system with JWT tokens",
  "status": "TODO",
  "priority": "HIGH",
  "assigneeId": null
}
```

#### Get All Tasks (with filtering and pagination)
```
GET /api/v1/tasks?status=TODO&assignee=1&page=0&size=10
```

Query Parameters:
- `status` (optional): Filter by task status
- `assignee` (optional): Filter by assignee ID
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

#### Get Specific Task
```
GET /api/v1/tasks/{taskId}
```

#### Update Task
```
PUT /api/v1/tasks/{taskId}
Content-Type: application/json

{
  "title": "Updated Task Title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "assigneeId": 2
}
```

#### Delete Task
```
DELETE /api/v1/tasks/{taskId}
```

#### Assign Task
```
POST /api/v1/tasks/{taskId}/assign?assigneeId=2
```

#### Update Task Status
```
PUT /api/v1/tasks/{taskId}/status?status=DONE
```

#### Get My Tasks (tasks assigned to current user)
```
GET /api/v1/tasks/my-tasks
```

#### Get Tasks Created by Me
```
GET /api/v1/tasks/created-by-me
```

#### Get Tasks by Status
```
GET /api/v1/tasks/status/{status}
```

### User Management Endpoints

#### Get All Users (for task assignment)
```
GET /api/v1/users
```

## Status Transitions

The API enforces the following status transitions:
- `TODO` → `IN_PROGRESS` or `DONE`
- `IN_PROGRESS` → `DONE`
- `DONE` → No further transitions allowed

## Authorization Rules

### Task Operations
- **Create Task**: Any authenticated user can create tasks
- **Update Task**: Only the creator or assignee can update a task
- **Delete Task**: Only the creator can delete a task
- **Assign Task**: Only the creator can assign a task to another user
- **Update Status**: Creator or assignee can update task status

### User Operations
- **Get Users**: Available to all authenticated users (for task assignment)

## Error Handling

The API provides standardized error responses:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": {
    "title": "Title is required"
  }
}
```

### Common HTTP Status Codes
- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Access denied
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Testing

Use the provided `http/task-management.http` file to test all endpoints. The file includes:

1. User registration and authentication
2. Task CRUD operations
3. Task assignment and status updates
4. Filtering and pagination examples

### Testing Steps
1. Start the application
2. Register a user using the registration endpoint
3. Login to get a JWT token
4. Use the token in the Authorization header for subsequent requests
5. Test all task management endpoints

## Database

The application uses H2 in-memory database by default. The database console is available at:
```
http://localhost:8080/h2-console
```

Database URL: `jdbc:h2:mem:jwt_security`
Username: `sa`
Password: (empty)

## Security Configuration

- JWT-based authentication
- Stateless session management
- CSRF disabled for API endpoints
- H2 console enabled for development
- Role-based access control for management endpoints

## Dependencies

- Spring Boot 3.1.4
- Spring Security 6.1.4
- Spring Data JPA
- H2 Database
- JWT (jjwt 0.11.5)
- Lombok
- Bean Validation
- SpringDoc OpenAPI 