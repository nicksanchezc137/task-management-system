# Task Management System - Frontend

A modern React-based task management application with authentication, task creation, editing, and filtering capabilities.

## Features

### Authentication
- User registration and login
- JWT token-based authentication
- Protected routes
- Automatic logout on token expiration

### Task Management
- Create, edit, and delete tasks
- Task status management (TODO, IN_PROGRESS, DONE)
- Priority levels (LOW, MEDIUM, HIGH)
- User assignment
- Inline task editing
- Task filtering by status and assignee

### User Interface
- Modern, responsive design with Tailwind CSS
- Kanban-style task board
- Modal forms for task creation
- Real-time form validation
- Loading states and error handling

## Tech Stack

- **React 19** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **Tailwind CSS v4** - Styling
- **React Router** - Client-side routing
- **React Hook Form** - Form management
- **Axios** - HTTP client

## Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── TaskCard.tsx    # Individual task display/edit
│   ├── CreateTaskModal.tsx # Task creation modal
│   ├── TaskFilters.tsx # Task filtering interface
│   └── ProtectedRoute.tsx # Authentication guard
├── pages/              # Page components
│   ├── Login.tsx       # Login page
│   ├── Register.tsx    # Registration page
│   └── Dashboard.tsx   # Main task board
├── context/            # React context
│   └── AuthContext.tsx # Authentication state
├── services/           # API services
│   └── api.ts         # HTTP client and API calls
├── types/              # TypeScript type definitions
│   └── index.ts       # Data model interfaces
└── utils/              # Utility functions
```

## Getting Started

### Prerequisites
- Node.js (v20.19.0 or higher)
- npm or yarn

### Installation

1. Clone the repository
2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

4. Open your browser and navigate to `http://localhost:5173`

### Backend API

This frontend expects a backend API running on `http://localhost:8080` with the following endpoints:

#### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

#### Tasks
- `GET /api/tasks?status=&assignee=` - Get tasks with optional filtering
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task

#### Users
- `GET /api/users` - Get all users for task assignment

## Data Models

### User
```typescript
interface User {
  id: string;
  username: string;
  email: string;
  role: 'ADMIN' | 'USER';
  createdAt: string;
}
```

### Task
```typescript
interface Task {
  id: string;
  title: string;
  description: string;
  status: 'TODO' | 'IN_PROGRESS' | 'DONE';
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  assigneeId: string | null;
  creatorId: string;
  createdAt: string;
  updatedAt: string;
  assignee?: User;
  creator?: User;
}
```

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## Environment Variables

The application is configured to connect to a backend API at `http://localhost:8080`. To change this, modify the `API_BASE_URL` constant in `src/services/api.ts`.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.
