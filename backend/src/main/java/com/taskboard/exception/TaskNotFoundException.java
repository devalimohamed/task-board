package com.taskboard.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("task not found: " + id);
    }
}
