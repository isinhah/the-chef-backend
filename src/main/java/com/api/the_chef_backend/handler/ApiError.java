package com.api.the_chef_backend.handler;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        String path,
        String message,
        int statusCode,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dateTime,
        List<FieldError> errors
) {
    public record FieldError(String field, String message) {
    }
}