package com.taskboard.service;

import com.taskboard.dto.CreateTaskRequest;
import com.taskboard.model.Bug;
import com.taskboard.model.Severity;
import com.taskboard.model.Task;
import com.taskboard.model.TaskStatus;
import com.taskboard.model.TaskType;
import com.taskboard.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Test
    void shouldCreateFeatureTaskWithDefaults() {
        TaskRepository repository = mock(TaskRepository.class);
        SimpMessageSendingOperations messagingTemplate = mock(SimpMessageSendingOperations.class);
        TaskService service = new TaskService(repository, messagingTemplate);

        CreateTaskRequest request = new CreateTaskRequest(
                TaskType.FEATURE,
                "Export dashboard",
                "Add PDF export",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task created = service.createTask(request);

        assertEquals("Export dashboard", created.getTitle());
        assertEquals(TaskStatus.TODO, created.getStatus());
        verify(repository).save(any(Task.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/tasks"), any(Map.class));
    }

    @Test
    void shouldUpdateTaskStatus() {
        TaskRepository repository = mock(TaskRepository.class);
        SimpMessageSendingOperations messagingTemplate = mock(SimpMessageSendingOperations.class);
        TaskService service = new TaskService(repository, messagingTemplate);

        Bug bug = new Bug("Login issue", Severity.HIGH);

        when(repository.findById(1L)).thenReturn(Optional.of(bug));
        when(repository.save(bug)).thenReturn(bug);

        Task updated = service.updateStatus(1L, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, updated.getStatus());
        verify(repository).save(bug);
    }
}
