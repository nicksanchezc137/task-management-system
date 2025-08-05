import React, { useState } from "react";
import type { Task, User } from "../types";
import { ROLES, TASK_STATUS } from "../general.constants";
import { useAuth } from "../context/AuthContext";

interface TaskCardProps {
  task: Task;
  users: User[];
  onUpdate: (id: string, updates: any) => Promise<void>;
  onDelete: (id: string) => Promise<void>;
}

export const TaskCard: React.FC<TaskCardProps> = ({
  task,
  users,
  onUpdate,
  onDelete,
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [editData, setEditData] = useState({
    title: task.title,
    description: task.description,
    status: task.status,
    priority: task.priority,
    assigneeId: task.assigneeId || "",
  } as {
    title: string;
    description: string;
    status: string;
    priority: string;
    assigneeId: string | null;
  });
  const { user } = useAuth();

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case "HIGH":
        return "bg-red-100 text-red-800";
      case "MEDIUM":
        return "bg-yellow-100 text-yellow-800";
      case "LOW":
        return "bg-green-100 text-green-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const handleSave = async () => {
    setIsLoading(true);
    try {
      await onUpdate(task.id, editData);
      setIsEditing(false);
    } catch (error) {
      console.error("Failed to update task:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async () => {
    if (window.confirm("Are you sure you want to delete this task?")) {
      setIsLoading(true);
      try {
        await onDelete(task.id);
      } catch (error) {
        console.error("Failed to delete task:", error);
      } finally {
        setIsLoading(false);
      }
    }
  };

  if (isEditing) {
    return (
      <div className="bg-white p-4 rounded-lg shadow border border-gray-200 min-w-[20rem]">
        <div className="space-y-3">
          <input
            type="text"
            value={editData.title}
            onChange={(e) =>
              setEditData({ ...editData, title: e.target.value })
            }
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Task title"
          />
          <textarea
            value={editData.description}
            onChange={(e) =>
              setEditData({ ...editData, description: e.target.value })
            }
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Task description"
            rows={3}
          />
          <div className="grid grid-cols-2 gap-2">
            <select
              value={editData.status}
              onChange={(e) =>
                setEditData({ ...editData, status: e.target.value as any })
              }
              className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value={TASK_STATUS.TODO}>To Do</option>
              <option value={TASK_STATUS.IN_PROGRESS}>In Progress</option>
              <option value={TASK_STATUS.DONE}>Done</option>
            </select>
            <select
              value={editData.priority}
              onChange={(e) =>
                setEditData({ ...editData, priority: e.target.value as any })
              }
              className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
            </select>
          </div>
          <select
            value={editData.assigneeId || ""}
            onChange={(e) =>
              setEditData({ ...editData, assigneeId: e.target.value || null })
            }
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value={task.assignee?.id || ""}>
              {task.assignee?.username || "Unassigned"}
            </option>
            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.username}
              </option>
            ))}
          </select>
          <div className="flex space-x-2">
            <button
              onClick={handleSave}
              disabled={isLoading}
              className="flex-1 bg-blue-600 text-white px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-700 disabled:opacity-50"
            >
              {isLoading ? "Saving..." : "Save"}
            </button>
            <button
              onClick={() => setIsEditing(false)}
              className="flex-1 bg-gray-300 text-gray-700 px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-400"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white p-4 rounded-lg shadow border border-gray-200 hover:shadow-md transition-shadow min-w-[20rem] md:min-w-[18rem] h-[18rem]">
      <div className="flex flex-col gap-4 sm:flex-row justify-between items-start mb-2">
        <h3 className="font-medium text-gray-900 text-sm text-left">
          {task.title}
        </h3>
        <div className="flex space-x-2">
          <button
            onClick={() => setIsEditing(true)}
            className="text-gray-400 hover:text-gray-600 text-xs"
          >
            Edit
          </button>
          <button
            onClick={handleDelete}
            className={`text-red-400 hover:text-red-600 text-xs ${user?.role === ROLES.ADMIN ? "" : "hidden"}`}
          >
            Delete
          </button>
        </div>
      </div>

      <p className="text-gray-600 text-sm mb-3 line-clamp-3 max-w-[26rem] text-left">
        {task.description}
      </p>

      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <span className="text-xs text-gray-500">Priority:</span>
          <span
            className={`px-2 py-1 rounded-full text-xs font-medium ${getPriorityColor(
              task.priority
            )}`}
          >
            {task.priority}
          </span>
        </div>

        <div className="flex items-center justify-between">
          <span className="text-xs text-gray-500">Assignee:</span>
          <span className="text-xs text-gray-700 w-[10rem] truncate text-right">
            {task.assignee?.username}
          </span>
        </div>

        <div className="flex items-center justify-between">
          <span className="text-xs text-gray-500">Created:</span>
          <span className="text-xs text-gray-700">
            {new Date(task.createdAt).toLocaleDateString()}
          </span>
        </div>
      </div>
    </div>
  );
};
