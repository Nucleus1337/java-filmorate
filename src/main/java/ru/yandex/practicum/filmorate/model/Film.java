package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;
import ru.yandex.practicum.filmorate.util.Sequence;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    @NonNull
    private long id;

    @NotBlank(message = "Не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длинна 200 символов")
    private String description;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private long duration;

    public Film(long id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = Sequence.getNextId();
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
