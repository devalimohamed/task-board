package com.taskboard.service;

import com.taskboard.dto.CreateTaskRequest;
import com.taskboard.model.*;
import com.taskboard.repository.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Test
    void shouldCreateFeatureTaskWithDefaults() {
        TaskRepository repository = mock(TaskRepository.class);
        TaskService service = new TaskService(repository);

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
    }

    @Test
    void shouldUpdateTaskStatus() {
        TaskRepository repository = mock(TaskRepository.class);
        TaskService service = new TaskService(repository);

        Bug bug = new Bug("Login issue", Severity.HIGH);

        when(repository.findById(1L)).thenReturn(Optional.of(bug));
        when(repository.save(bug)).thenReturn(bug);

        Task updated = service.updateStatus(1L, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, updated.getStatus());
    }
}
