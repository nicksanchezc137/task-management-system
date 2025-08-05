# Task Management System

A full-stack task management application built with Spring Boot and React, featuring JWT authentication, role-based access control, and a modern user interface.

## 🚀 Overview

This project consists of two main components:
- **Backend**: Spring Boot 3.0 REST API with JWT authentication
- **Frontend**: React 19 application with TypeScript and Tailwind CSS

## 🏗️ Architecture

### Backend (`/backend`)
- **Framework**: Spring Boot 3.0 with Spring Security
- **Authentication**: JWT-based with refresh tokens
- **Database**: H2 in-memory database (development)
- **Build Tool**: Maven
- **Key Features**:
  - Role-based access control (ADMIN/USER)
  - Complete CRUD operations for tasks
  - User management and authentication
  - CORS support for frontend integration

### Frontend (`/frontend`)
- **Framework**: React 19 with TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS v4
- **Key Features**:
  - Modern, responsive UI
  - Kanban-style task board
  - Real-time form validation
  - Protected routes with authentication

## 🎯 Features

### Authentication & Authorization
- JWT token-based authentication
- Role-based access control (ADMIN/USER)
- Protected routes and API endpoints
- Automatic token refresh

### Task Management
- Create, read, update, and delete tasks
- Task assignment to users
- Status tracking (TODO, IN_PROGRESS, COMPLETED)
- Priority levels (LOW, MEDIUM, HIGH)
- Advanced filtering

### User Interface
- Modern, responsive design
- Modal forms for task creation
- Inline task editing
- Real-time validation
- Loading states and error handling

## 🛠️ Technology Stack

### Backend
- Spring Boot 3.0
- Spring Security
- Spring Data JPA
- JWT (JSON Web Tokens)
- H2 Database
- Maven
- Lombok

### Frontend
- React 19
- TypeScript
- Vite
- Tailwind CSS v4
- React Router
- React Hook Form
- Axios

## 🚀 Quick Start

### Prerequisites
- JDK 17+
- Node.js 20.19.0+
- Maven 3+

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
The API will be available at `http://localhost:8080`

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
The application will be available at `http://localhost:5173`

### Default Credentials
- **Admin**: `admin` / `password`
- **User**: `developer` / `password`

## 📚 Documentation

- **[Backend API Documentation](./backend/README.md)** - Complete API reference, authentication flow, and database schema
- **[Frontend Documentation](./frontend/README.md)** - UI components, data models, and development setup

## 🔐 Security Features

- BCrypt password hashing
- JWT token revocation
- CORS configuration
- Method-level security with `@PreAuthorize`
- Granular permissions system

## 📊 Database Schema

The system includes three main entities:
- **Users**: Authentication and role management
- **Tasks**: Task data with assignment and status tracking
- **Tokens**: JWT token storage for revocation

## 🌐 API Endpoints

The backend provides comprehensive REST APIs for:
- Authentication (register, login, logout, refresh)
- Task management (CRUD operations, assignment, status updates)
- User management (profile, password changes)

## 🎨 UI Components

The frontend includes reusable components:
- Task cards with inline editing
- Modal forms for task creation
- Filtering interface
- Protected route wrapper

## 📝 Development Notes

- **Swagger/OpenAPI**: Available at `http://localhost:8080/swagger-ui.html`
- **Seed Data**: Automatically loaded with sample users and tasks
- **CORS**: Configured for common development ports
- **Logging**: DEBUG level for security components

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

