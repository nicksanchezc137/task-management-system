import React, { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { tasksAPI, usersAPI } from "../services/api";
import type { Task, User } from "../types";
import { TaskFilters } from "../components/TaskFilters";
import { TaskCard } from "../components/TaskCard";
import { CreateTaskModal } from "../components/CreateTaskModal";
import { TASK_STATUS, TASK_STATUS_LABELS } from "../general.constants";
export const Dashboard: React.FC = () => {
  const { logout, user } = useAuth();
  const [tasks, setTasks] = useState<Task[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [filters, setFilters] = useState({ status: "", assignee: "" });
  const [selectedView, setSelectedView] = useState<keyof typeof TASK_STATUS>(TASK_STATUS.TODO);

  useEffect(() => {
    loadData();
  }, [filters]);

  const loadData = async () => {
    try {
      setIsLoading(true);
      const [tasksData, usersData] = await Promise.all([
        tasksAPI.getTasks(filters, user),
        usersAPI.getUsers(),
      ]);
      setTasks(tasksData);
      setUsers(usersData);
    } catch (error) {
      console.error("Failed to load data:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreateTask = async (taskData: any) => {
    try {
      const newTask = await tasksAPI.createTask({
        ...taskData,
        status: TASK_STATUS.TODO,
      });
      setTasks((prev) => [...prev, newTask]);
      setIsCreateModalOpen(false);
    } catch (error) {
      console.error("Failed to create task:", error);
    }
  };

  const handleUpdateTask = async (id: string, updates: any) => {
    try {
      const updatedTask = await tasksAPI.updateTask(id, updates);
      setTasks((prev) =>
        prev.map((task) => (task.id === id ? updatedTask : task))
      );
    } catch (error) {
      console.error("Failed to update task:", error);
    }
  };

  const handleDeleteTask = async (id: string) => {
    try {
      await tasksAPI.deleteTask(id);
      setTasks((prev) => prev.filter((task) => task.id !== id));
    } catch (error) {
      console.error("Failed to delete task:", error);
    }
  };

  const getTasksByStatus = (status: string) => {
    return tasks.filter((task) => task.status === status);
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center py-6">
            <div>
              <h3 className="text-2xl font-bold text-gray-900">
                Task Management
              </h3>
            </div>
            <div className="flex mt-4 sm:mt-0 items-center space-x-4">
              <button
                onClick={() => setIsCreateModalOpen(true)}
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md font-medium"
              >
                Create Task
              </button>
              <button
                onClick={logout}
                className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto py-6">
        <TaskFilters
          filters={filters}
          onFiltersChange={setFilters}
          users={users}
        />
        
        <div className="mt-4 lg:hidden">
          <label htmlFor="view-toggle" className="block text-sm font-medium text-gray-700 mb-2 text-left">
            View
          </label>
          <select
            id="view-toggle"
            value={selectedView}
            onChange={(e) => setSelectedView(e.target.value as keyof typeof TASK_STATUS)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value={TASK_STATUS.TODO}>{TASK_STATUS_LABELS.TODO}</option>
            <option value={TASK_STATUS.IN_PROGRESS}>{TASK_STATUS_LABELS.IN_PROGRESS}</option>
            <option value={TASK_STATUS.DONE}>{TASK_STATUS_LABELS.DONE}</option>
          </select>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className={`bg-gray-100 rounded-lg p-4 min-w-[20rem] min-h-[19rem] flex flex-col ${selectedView === TASK_STATUS.TODO ? 'block' : 'hidden lg:block'}`}>
            <h2 className="text-lg font-semibold text-gray-900 mb-4">To Do</h2>
            <div className="space-y-3 flex flex-col items-center justify-center">
              {getTasksByStatus(TASK_STATUS.TODO).map((task) => (
                <TaskCard
                  key={task.id}
                  task={task}
                  users={users}
                  onUpdate={handleUpdateTask}
                  onDelete={handleDeleteTask}
                />
              ))}
            </div>
          </div>

          <div className={`bg-gray-100 rounded-lg p-4 min-w-[20rem] min-h-[19rem] flex flex-col ${selectedView === TASK_STATUS.IN_PROGRESS ? 'block' : 'hidden lg:block'}`}>
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
              In Progress
            </h2>
            <div className="space-y-3 flex flex-col items-center justify-center">
              {getTasksByStatus(TASK_STATUS.IN_PROGRESS).map((task) => (
                <TaskCard
                  key={task.id}
                  task={task}
                  users={users}
                  onUpdate={handleUpdateTask}
                  onDelete={handleDeleteTask}
                />
              ))}
            </div>
          </div>

          <div className={`bg-gray-100 rounded-lg p-4 min-w-[20rem] min-h-[19rem] flex flex-col ${selectedView === TASK_STATUS.DONE ? 'block' : 'hidden lg:block'}`}>
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Done</h2>
            <div className="space-y-3 flex flex-col items-center justify-center">
              {getTasksByStatus(TASK_STATUS.DONE).map((task) => (
                <TaskCard
                  key={task.id}
                  task={task}
                  users={users}
                  onUpdate={handleUpdateTask}
                  onDelete={handleDeleteTask}
                />
              ))}
            </div>
          </div>
        </div>
      </div>

      <CreateTaskModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        onSubmit={handleCreateTask}
        users={users}
      />
    </div>
  );
};
