package ru.yandex.practicum.filmorate.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorResponse {
    private final List<ValidationError> validationErrors = new ArrayList<>();
}
