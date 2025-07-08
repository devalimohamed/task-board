package com.taskboard.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue("EPIC")
public class Epic extends Task {

    private LocalDate targetStartDate;
    private LocalDate targetEndDate;

    @Column(length = 2000)
    private String businessObjective;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "epic_id")
    private List<Task> childTasks = new ArrayList<>();

    public Epic() {
        super();
    }

    public Epic(String title) {
        super(title);
    }

    @Override
    public String getTaskTypeDescription() {
        return "Epic - Large feature spanning multiple tasks";
    }

    @Override
    public int getEstimatedEffortHours() {
        if (childTasks.isEmpty()) {
            return 40;
        }
        return childTasks.stream().mapToInt(Task::getEstimatedEffortHours).sum();
    }

    @Override
    public String getIcon() {
        return "🏆";
    }

    public void addChildTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        if (task == this) {
            throw new IllegalArgumentException("an epic cannot contain itself as a child task");
        }
        if (!childTasks.contains(task)) {
            childTasks.add(task);
        }
    }

    public int getProgressPercentage() {
        if (childTasks.isEmpty()) {
            return 0;
        }
        long completedCount = childTasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        return (int) ((completedCount * 100) / childTasks.size());
    }

    public LocalDate getTargetStartDate() {
        return targetStartDate;
    }

    public void setTargetStartDate(LocalDate targetStartDate) {
        this.targetStartDate = targetStartDate;
        validateTimeline();
    }

    public LocalDate getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(LocalDate targetEndDate) {
        this.targetEndDate = targetEndDate;
        validateTimeline();
    }

    public String getBusinessObjective() {
        return businessObjective;
    }

    public void setBusinessObjective(String businessObjective) {
        this.businessObjective = businessObjective;
    }

    public List<Task> getChildTasks() {
        return Collections.unmodifiableList(childTasks);
    }

    private void validateTimeline() {
        if (targetStartDate != null && targetEndDate != null && targetEndDate.isBefore(targetStartDate)) {
            throw new IllegalArgumentException("targetEndDate must be on or after targetStartDate");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " - Progress: " + getProgressPercentage() + "%";
    }
}
