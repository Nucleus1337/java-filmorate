package ru.yandex.practicum.filmorate.exception;

import lombok.Data;

@Data
public class ValidationError {
    private final String fieldName;
    private final String message;
}
