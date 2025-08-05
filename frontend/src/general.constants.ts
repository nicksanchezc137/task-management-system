export const TASK_STATUS = {
  TODO: "TODO",
  IN_PROGRESS: "IN_PROGRESS",
  DONE: "DONE",
} as const;

export const TASK_STATUS_LABELS = {
  TODO: "To Do",
  IN_PROGRESS: "In Progress",
  DONE: "Done",
} as const;

export const API_BASE_URL = "http://localhost:8080/api/v1";

export const ROLES = {
  ADMIN: "ADMIN",
  USER: "USER",
};
