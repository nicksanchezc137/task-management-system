import axios from 'axios';
import type { 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse, 
  User, 
  Task, 
  CreateTaskRequest, 
  UpdateTaskRequest, 
  TaskFilters 
} from '../types';
import { API_BASE_URL, ROLES } from '../general.constants';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle token expiration
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/login', credentials);
    return response.data;
  },

  register: async (userData: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/register', userData);
    return response.data;
  },
};

// Tasks API
export const tasksAPI = {
  getTasks: async (filters?: TaskFilters, user: User | null = null): Promise<Task[]> => {
    try{
    const params = new URLSearchParams();
    if (filters?.status) params.append('status', filters.status);

    if(user?.role === ROLES.USER) params.append('assignee', user?.id?.toString() || '');

    if (filters?.assignee && user?.role === ROLES.ADMIN){
        params.append('assignee', filters.assignee);
    }
    
    const response = await api.get(`/tasks?${params.toString()}`);
    return response.data?.content || [];
    }
    catch(e){
        return [];
    }
  },

  createTask: async (taskData: CreateTaskRequest): Promise<Task> => {
    const response = await api.post('/tasks', taskData);
    return response.data;
  },

  updateTask: async (id: string, taskData: UpdateTaskRequest): Promise<Task> => {
    const response = await api.put(`/tasks/${id}`, taskData);
    return response.data;
  },

  deleteTask: async (id: string): Promise<void> => {
    await api.delete(`/tasks/${id}`);
  },
};

export const usersAPI = {
  getUsers: async (): Promise<User[]> => {
    const response = await api.get('/users');
    return response.data;
  },
};

export default api; 