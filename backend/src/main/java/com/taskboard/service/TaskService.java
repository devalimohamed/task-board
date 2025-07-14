package com.taskboard.service;

import com.taskboard.dto.CreateTaskRequest;
import com.taskboard.exception.TaskNotFoundException;
import com.taskboard.model.*;
import com.taskboard.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task createTask(CreateTaskRequest request) {
        Task task = switch (request.taskType()) {
            case BUG -> createBug(request);
            case FEATURE -> createFeature(request);
            case EPIC -> createEpic(request);
            case SUBTASK -> createSubtask(request);
        };

        task.setDescription(request.description());
        task.setDueDate(request.dueDate());

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task updateStatus(Long id, TaskStatus status) {
        Task task = getTask(id);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    private Bug createBug(CreateTaskRequest request) {
        Severity severity = request.severity() == null ? Severity.MEDIUM : request.severity();
        return new Bug(request.title(), severity);
    }

    private Feature createFeature(CreateTaskRequest request) {
        Priority priority = request.priority() == null ? Priority.MEDIUM : request.priority();
        Feature feature = new Feature(request.title(), priority);
        feature.setStoryPoints(request.storyPoints());
        return feature;
    }

    private Epic createEpic(CreateTaskRequest request) {
        Epic epic = new Epic(request.title());
        epic.setBusinessObjective(request.businessObjective());
        epic.setTargetStartDate(request.targetStartDate());
        epic.setTargetEndDate(request.targetEndDate());
        return epic;
    }

    private Subtask createSubtask(CreateTaskRequest request) {
        if (request.estimatedMinutes() == null) {
            throw new IllegalArgumentException("estimatedMinutes is required for SUBTASK");
        }

        Task parentTask = null;
        if (request.parentTaskId() != null) {
            parentTask = taskRepository.findById(request.parentTaskId())
                    .orElseThrow(() -> new TaskNotFoundException(request.parentTaskId()));
        }

        return new Subtask(request.title(), parentTask, request.estimatedMinutes());
    }
}
