package com.taskboard.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("BUG")
public class Bug extends Task {

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(length = 4000)
    private String stepsToReproduce;

    @Column(length = 2000)
    private String expectedBehavior;

    @Column(length = 2000)
    private String actualBehavior;

    public Bug() {
        super();
    }

    public Bug(String title, Severity severity) {
        super(title);
        setSeverity(severity);
    }

    @Override
    public String getTaskTypeDescription() {
        return "Bug - Something that needs fixing";
    }

    @Override
    public int getEstimatedEffortHours() {
        if (severity == null) {
            return 4;
        }

        return switch (severity) {
            case LOW -> 2;
            case MEDIUM -> 4;
            case HIGH -> 8;
            case CRITICAL -> 16;
        };
    }

    @Override
    public String getIcon() {
        return "🐞";
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        if (severity == null) {
            throw new IllegalArgumentException("severity must not be null");
        }
        this.severity = severity;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }

    public void setStepsToReproduce(String stepsToReproduce) {
        this.stepsToReproduce = stepsToReproduce;
    }

    public String getExpectedBehavior() {
        return expectedBehavior;
    }

    public void setExpectedBehavior(String expectedBehavior) {
        this.expectedBehavior = expectedBehavior;
    }

    public String getActualBehavior() {
        return actualBehavior;
    }

    public void setActualBehavior(String actualBehavior) {
        this.actualBehavior = actualBehavior;
    }

    @Override
    public String toString() {
        return super.toString() + " - Severity: " + severity;
    }
}
