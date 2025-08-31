import { Task } from "./types";

const API_BASE = import.meta.env.VITE_API_BASE ?? "http://localhost:8080";

export async function getTasks(): Promise<Task[]> {
  const response = await fetch(`${API_BASE}/api/tasks`);
  if (!response.ok) {
    throw new Error("Failed to fetch tasks");
  }

  return response.json();
}

export async function createFeature(title: string): Promise<void> {
  const response = await fetch(`${API_BASE}/api/tasks`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      taskType: "FEATURE",
      title,
      priority: "MEDIUM"
    })
  });

  if (!response.ok) {
    throw new Error("Failed to create task");
  }
}
