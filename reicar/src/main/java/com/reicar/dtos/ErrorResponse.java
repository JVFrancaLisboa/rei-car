package com.reicar.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public ErrorResponse{
        Objects.requireNonNull(timestamp, "timestamp must not be null");
        if (status < 100 || status > 599)  throw new IllegalArgumentException("Invalid HttpStatus code");
    }

    public static ErrorResponse of(int status, String error, String message, String path){
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path);
    }
}
