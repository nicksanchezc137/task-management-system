# Task Management System API

A comprehensive task management system built with Spring Boot 3.0, featuring JWT authentication, role-based access control, and a complete task management workflow.

## Features

* **JWT Authentication**: Secure token-based authentication with refresh tokens
* **Role-Based Access Control**: Granular permissions for different user roles
* **Task Management**: Full CRUD operations for tasks with assignment capabilities
* **User Management**: User registration, profile management, and role assignment
* **CORS Support**: Configured for frontend integration (React, Vue, etc.)
* **H2 Database**: In-memory database for development with automatic schema creation
* **Seed Data**: Pre-populated with sample users and tasks

## Technologies

* **Spring Boot 3.0** - Main framework
* **Spring Security** - Authentication and authorization
* **JWT (JSON Web Tokens)** - Stateless authentication
* **Spring Data JPA** - Database operations
* **H2 Database** - In-memory database
* **Maven** - Build tool
* **Lombok** - Reduces boilerplate code

## Getting Started

### Prerequisites
* JDK 17+
* Maven 3+

### Running the Application
```bash
# Clone the repository
git clone <repository-url>

# Navigate to the project directory
cd task-management-system/backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at **http://localhost:8080**

### Default Seed Data
The application comes with pre-configured users:
- **Admin**: `admin` / `password`
- **User**: `developer` / `password`

## JWT Implementation

### Token Structure
The JWT implementation uses a stateless approach with the following components:

1. **Access Token**: Short-lived token (24 hours) for API access
2. **Refresh Token**: Long-lived token (7 days) for obtaining new access tokens
3. **Token Storage**: Tokens are stored in the database for revocation capability

### Authentication Flow
1. **Registration/Login**: User provides credentials
2. **Token Generation**: System generates both access and refresh tokens
3. **Token Storage**: Tokens are saved to database with user association
4. **API Access**: Client includes access token in Authorization header
5. **Token Validation**: Each request validates token signature and expiration
6. **Token Refresh**: Client can use refresh token to get new access token

### Security Features
- **Password Hashing**: BCrypt with salt rounds
- **Token Revocation**: Logout invalidates all user tokens
- **CORS Configuration**: Configured for frontend integration
- **Method-Level Security**: `@PreAuthorize` annotations for fine-grained control

## Roles and Permissions

### Role Hierarchy
The system implements a two-tier role system with granular permissions:

#### ADMIN Role
**Full system access with all capabilities:**
- `TASK_READ_ALL` - Read all tasks in the system
- `TASK_UPDATE_ALL` - Update any task
- `TASK_CREATE_ALL` - Create tasks (inherited from USER)
- `TASK_DELETE_ALL` - Delete any task
- `TASK_ASSIGN` - Assign tasks to any user
- `USER_READ_ALL` - View all users in the system

#### USER Role
**Limited access focused on own tasks:**
- `TASK_READ_OWN` - Read only tasks assigned to them
- `TASK_UPDATE_OWN` - Update only tasks assigned to them
- `TASK_CREATE` - Create new tasks
- `USER_READ_ALL` - View all users in the system

### Permission Matrix

| Operation | ADMIN | USER |
|-----------|-------|------|
| Create Task | ✅ | ✅ |
| Read Own Tasks | ✅ | ✅ |
| Read All Tasks | ✅ | ❌ |
| Update Own Tasks | ✅ | ✅ |
| Update Any Task | ✅ | ❌ |
| Delete Tasks | ✅ | ❌ |
| Assign Tasks | ✅ | ❌ (can self-assign) |
| View All Users | ✅ | ✅ |

## API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

#### Refresh Token
```http
POST /api/v1/auth/refresh-token
Authorization: Bearer <refresh_token>
```

#### Logout
```http
POST /api/v1/auth/logout
Authorization: Bearer <access_token>
```

### Task Management Endpoints

#### Create Task
```http
POST /api/v1/tasks
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "title": "Implement User Authentication",
  "description": "Create secure JWT-based authentication system",
  "status": "TODO",
  "priority": "HIGH",
  "assigneeId": 2
}
```

**Permissions**: `TASK_CREATE` (USER, ADMIN)

#### Get All Tasks (with filtering)
```http
GET /api/v1/tasks?status=IN_PROGRESS&priority=HIGH&assignee=2&page=0&size=10
Authorization: Bearer <access_token>
```

**Permissions**: 
- `TASK_READ_ALL` (ADMIN) - Can see all tasks
- `TASK_READ_OWN` (USER) - Can only see own tasks when filtering by assignee

#### Get Task by ID
```http
GET /api/v1/tasks/{taskId}
Authorization: Bearer <access_token>
```

**Permissions**:
- `TASK_READ_ALL` (ADMIN) - Can read any task
- `TASK_READ_OWN` (USER) - Can only read tasks assigned to them

#### Update Task
```http
PUT /api/v1/tasks/{taskId}
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "title": "Updated Task Title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "assigneeId": 3
}
```

**Permissions**:
- `TASK_UPDATE_ALL` (ADMIN) - Can update any task
- `TASK_UPDATE_OWN` (USER) - Can only update tasks assigned to them

#### Delete Task
```http
DELETE /api/v1/tasks/{taskId}
Authorization: Bearer <access_token>
```

**Permissions**: `TASK_DELETE_ALL` (ADMIN only)

#### Assign Task
```http
POST /api/v1/tasks/{taskId}/assign
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "assigneeId": 2
}
```

**Permissions**:
- `TASK_ASSIGN` (ADMIN) - Can assign tasks to any user
- `TASK_READ_OWN` (USER) - Can self-assign tasks

#### Update Task Status
```http
PUT /api/v1/tasks/{taskId}/status
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "status": "COMPLETED"
}
```

**Permissions**:
- `TASK_UPDATE_ALL` (ADMIN) - Can update status of any task
- `TASK_UPDATE_OWN` (USER) - Can only update status of own tasks

#### Get My Tasks
```http
GET /api/v1/tasks/my-tasks
Authorization: Bearer <access_token>
```

**Permissions**: `TASK_READ_OWN` (USER, ADMIN)

#### Get Tasks Created by Me
```http
GET /api/v1/tasks/created-by-me
Authorization: Bearer <access_token>
```

**Permissions**: `TASK_READ_OWN` (USER, ADMIN)

#### Get Tasks by Status
```http
GET /api/v1/tasks/status/{status}
Authorization: Bearer <access_token>
```

**Permissions**: `TASK_READ_ALL` (ADMIN only)

### User Management Endpoints

#### Get All Users
```http
GET /api/v1/users
Authorization: Bearer <access_token>
```

**Permissions**: `USER_READ_ALL` (USER, ADMIN)

#### Change Password
```http
PATCH /api/v1/users
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "currentPassword": "oldpassword",
  "newPassword": "newpassword"
}
```

**Permissions**: Authenticated users can change their own password

#### Debug Endpoints

##### Get My Permissions
```http
GET /api/v1/users/my-permissions
Authorization: Bearer <access_token>
```

Returns current user's authorities and authentication details.

##### Test Authentication
```http
GET /api/v1/users/test-auth
Authorization: Bearer <access_token>
```

Simple endpoint to test if authentication is working.

## Request/Response Examples

### Successful Login Response
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@mail.com",
    "role": "ADMIN"
  }
}
```

### Task Response
```json
{
  "id": 1,
  "title": "Implement User Authentication",
  "description": "Create secure JWT-based authentication system",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "assignee": {
    "id": 2,
    "username": "developer",
    "email": "developer@mail.com",
    "role": "USER"
  },
  "creator": {
    "id": 1,
    "username": "admin",
    "email": "admin@mail.com",
    "role": "ADMIN"
  },
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:45:00"
}
```

## Error Handling

The API returns consistent error responses:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied",
  "details": "Insufficient permissions for this operation"
}
```

Common HTTP status codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `500` - Internal Server Error

## CORS Configuration

The API is configured to accept requests from:
- `http://localhost:5173` (Vite default)
- `http://localhost:3000` (Create React App default)
- `http://localhost:4173` (Vite preview)

## Database Schema

### Users Table
- `id` (Primary Key)
- `username` (Unique)
- `email` (Unique)
- `password` (BCrypt hashed)
- `role` (ENUM: USER, ADMIN)

### Tasks Table
- `id` (Primary Key)
- `title`
- `description`
- `status` (ENUM: TODO, IN_PROGRESS, COMPLETED)
- `priority` (ENUM: LOW, MEDIUM, HIGH)
- `assignee_id` (Foreign Key to Users)
- `creator_id` (Foreign Key to Users)
- `created_at`
- `updated_at`

### Tokens Table
- `id` (Primary Key)
- `token` (JWT string)
- `token_type` (ENUM: BEARER)
- `expired` (Boolean)
- `revoked` (Boolean)
- `user_id` (Foreign Key to Users)

## Development Notes
- **Swagger/OpenAPI**: Available at `http://localhost:8080/swagger-ui.html`
- **Logging**: Configured for DEBUG level on security components
- **Seed Data**: Automatically loaded on application startup
