import React from "react";
import type { User } from "../types";
import { ROLES, TASK_STATUS, TASK_STATUS_LABELS } from "../general.constants";
import { useAuth } from "../context/AuthContext";

interface TaskFiltersProps {
  filters: {
    status: string;
    assignee: string;
  };
  onFiltersChange: (filters: { status: string; assignee: string }) => void;
  users: User[];
}

export const TaskFilters: React.FC<TaskFiltersProps> = ({
  filters,
  onFiltersChange,
  users,
}) => {
  const { user } = useAuth();
  const handleStatusChange = (status: string) => {
    onFiltersChange({ ...filters, status });
  };

  const handleAssigneeChange = (assignee: string) => {
    onFiltersChange({ ...filters, assignee });
  };

  const clearFilters = () => {
    onFiltersChange({ status: "", assignee: "" });
  };

  return (
    <div className="bg-white p-4 rounded-lg shadow border border-gray-200 w-full">
      <div className="flex flex-col sm:flex-row gap-4 items-start">
        <div className="flex-1 min-w-0">
          <label
            htmlFor="status-filter"
            className="block text-sm font-medium text-gray-700 mb-1 text-left"
          >
            Filter by Status
          </label>
          <select
            id="status-filter"
            value={filters.status}
            onChange={(e) => handleStatusChange(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">All Statuses</option>
            <option value={TASK_STATUS.TODO}>To Do</option>
            <option value={TASK_STATUS.IN_PROGRESS}>In Progress</option>
            <option value={TASK_STATUS.DONE}>Done</option>
          </select>
        </div>
        {user?.role === ROLES.ADMIN && (
          <div className="flex-1 min-w-0">
            <label
              htmlFor="assignee-filter"
              className="block text-sm font-medium text-gray-700 mb-1 text-left"
            >
              Filter by Assignee
            </label>
            <select
              id="assignee-filter"
              value={filters.assignee}
              onChange={(e) => handleAssigneeChange(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Assignees</option>
              <option value="0">Unassigned</option>
              {users.map((user) => (
                <option key={user.id} value={user.id}>
                  {user.username}
                </option>
              ))}
            </select>
          </div>
        )}

        <div className="flex-shrink-0">
          <button
            onClick={clearFilters}
            className="px-4 py-2 bg-gray-300 text-gray-700 rounded-md font-medium hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500 mt-5 whitespace-nowrap"
          >
            Clear Filters
          </button>
        </div>
      </div>

      {(filters.status || filters.assignee) && (
        <div className="mt-4 flex flex-col sm:flex-row flex-wrap gap-2 min-h-[2rem] items-start sm:items-center">
          <span className="text-sm text-gray-600">Active filters:</span>
          {filters.status && (
            <span className="inline-flex items-center px-2.5 py-2.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
              Status:{" "}
              {
                TASK_STATUS_LABELS[
                  filters.status as keyof typeof TASK_STATUS_LABELS
                ]
              }
              <span
                onClick={() => handleStatusChange("")}
                className="ml-1 inline-flex items-center justify-center w-4 h-4 rounded-full text-blue-400 hover:bg-blue-200 hover:text-red-500 cursor-pointer"
              >
                ×
              </span>
            </span>
          )}
          {filters.assignee && (
            <span className="inline-flex items-center px-2.5 py-2.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
              Assignee:{" "}
              {filters.assignee === "unassigned"
                ? "Unassigned"
                : users.find((u) => +u.id === +filters.assignee)?.username}
              <span
                onClick={() => handleAssigneeChange("")}
                className="ml-1 inline-flex items-center justify-center w-4 h-4 rounded-full text-green-400 hover:bg-red-200 hover:text-green-500 cursor-pointer"
              >
                ×
              </span>
            </span>
          )}
        </div>
      )}
    </div>
  );
};
