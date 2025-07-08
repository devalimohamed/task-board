package com.taskboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("SUBTASK")
public class Subtask extends Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    @JsonIgnore
    private Task parentTask;

    private Integer estimatedMinutes;

    public Subtask() {
        super();
    }

    public Subtask(String title, Task parentTask, int estimatedMinutes) {
        super(title);
        setParentTask(parentTask);
        setEstimatedMinutes(estimatedMinutes);
    }

    @Override
    public String getTaskTypeDescription() {
        return "Subtask - Smaller unit of work";
    }

    @Override
    public int getEstimatedEffortHours() {
        return (estimatedMinutes + 59) / 60;
    }

    @Override
    public String getIcon() {
        return "🧩";
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(Integer estimatedMinutes) {
        if (estimatedMinutes == null || estimatedMinutes <= 0) {
            throw new IllegalArgumentException("estimatedMinutes must be > 0");
        }
        this.estimatedMinutes = estimatedMinutes;
    }

    @Override
    public String toString() {
        return super.toString() + " - Estimated Minutes: " + estimatedMinutes;
    }
}
