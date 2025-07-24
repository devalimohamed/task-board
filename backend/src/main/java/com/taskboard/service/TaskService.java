package com.taskboard.service;

import com.taskboard.dto.CreateTaskRequest;
import com.taskboard.exception.TaskNotFoundException;
import com.taskboard.model.Bug;
import com.taskboard.model.Epic;
import com.taskboard.model.Feature;
import com.taskboard.model.Priority;
import com.taskboard.model.Severity;
import com.taskboard.model.Subtask;
import com.taskboard.model.Task;
import com.taskboard.model.TaskStatus;
import com.taskboard.repository.TaskRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public TaskService(TaskRepository taskRepository, SimpMessageSendingOperations messagingTemplate) {
        this.taskRepository = taskRepository;
        this.messagingTemplate = messagingTemplate;
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

        Task saved = taskRepository.save(task);
        broadcastChange("CREATED", saved.getId());
        return saved;
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
        Task saved = taskRepository.save(task);
        broadcastChange("STATUS_UPDATED", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
        broadcastChange("DELETED", id);
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

    private void broadcastChange(String event, Long taskId) {
        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("event", event);
        payload.put("taskId", taskId);
        messagingTemplate.convertAndSend("/topic/tasks", payload);
    }
}
