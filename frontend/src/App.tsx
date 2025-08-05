import { useEffect, useMemo, useState } from "react";
import { createFeature, getTasks } from "./api";
import { Task } from "./types";

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [title, setTitle] = useState("");
  const [error, setError] = useState("");

  const grouped = useMemo(() => {
    return {
      TODO: tasks.filter((t) => t.status === "TODO"),
      IN_PROGRESS: tasks.filter((t) => t.status === "IN_PROGRESS"),
      DONE: tasks.filter((t) => t.status === "DONE")
    };
  }, [tasks]);

  async function load() {
    try {
      setError("");
      setTasks(await getTasks());
    } catch (err) {
      setError((err as Error).message);
    }
  }

  async function onCreate() {
    if (!title.trim()) {
      return;
    }

    try {
      await createFeature(title.trim());
      setTitle("");
      await load();
    } catch (err) {
      setError((err as Error).message);
    }
  }

  useEffect(() => {
    load();
  }, []);

  return (
    <main className="page">
      <header className="header">
        <h1>Task Board</h1>
        <p>Simple status view</p>
      </header>

      <section className="create-row">
        <input
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="New feature title"
        />
        <button onClick={onCreate}>Create</button>
      </section>

      {error ? <p className="error">{error}</p> : null}

      <section className="board">
        <Column title="TODO" tasks={grouped.TODO} />
        <Column title="IN_PROGRESS" tasks={grouped.IN_PROGRESS} />
        <Column title="DONE" tasks={grouped.DONE} />
      </section>
    </main>
  );
}

function Column({ title, tasks }: { title: string; tasks: Task[] }) {
  return (
    <article className="column">
      <h2>{title}</h2>
      {tasks.map((task) => (
        <div key={task.id} className="card">
          <strong>{task.title}</strong>
          <small>{task.status}</small>
        </div>
      ))}
    </article>
  );
}

export default App;
