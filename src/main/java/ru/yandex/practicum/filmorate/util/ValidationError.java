package ru.yandex.practicum.filmorate.util;

import lombok.Data;

@Data
public class ValidationError {
    private final String fieldName;
    private final String message;
}
