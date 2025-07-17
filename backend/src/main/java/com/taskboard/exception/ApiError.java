package com.taskboard.exception;

import java.time.LocalDateTime;

public record ApiError(LocalDateTime timestamp, String message) {
}
