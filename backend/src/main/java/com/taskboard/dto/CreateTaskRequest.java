package com.taskboard.dto;

import com.taskboard.model.Priority;
import com.taskboard.model.Severity;
import com.taskboard.model.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateTaskRequest(
        @NotNull TaskType taskType,
        @NotBlank String title,
        String description,
        LocalDateTime dueDate,
        Severity severity,
        Priority priority,
        Integer storyPoints,
        String businessObjective,
        LocalDate targetStartDate,
        LocalDate targetEndDate,
        Long parentTaskId,
        Integer estimatedMinutes
) {
}
