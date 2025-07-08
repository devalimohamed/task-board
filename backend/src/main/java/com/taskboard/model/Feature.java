package com.taskboard.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("FEATURE")
public class Feature extends Task {

    @Column(length = 4000)
    private String acceptanceCriteria;

    private Integer storyPoints;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(length = 2000)
    private String businessValue;

    public Feature() {
        super();
    }

    public Feature(String title, Priority priority) {
        super(title);
        setPriority(priority);
    }

    @Override
    public String getTaskTypeDescription() {
        return "Feature - New functionality to implement";
    }

    @Override
    public int getEstimatedEffortHours() {
        if (storyPoints == null) {
            return 8;
        }

        return storyPoints * 4;
    }

    @Override
    public String getIcon() {
        return "✨";
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public Integer getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(Integer storyPoints) {
        if (storyPoints != null && storyPoints <= 0) {
            throw new IllegalArgumentException("storyPoints must be greater than zero");
        }
        this.storyPoints = storyPoints;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("priority must not be null");
        }
        this.priority = priority;
    }

    public String getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(String businessValue) {
        this.businessValue = businessValue;
    }

    @Override
    public String toString() {
        return super.toString() + " - Priority: " + priority;
    }
}
