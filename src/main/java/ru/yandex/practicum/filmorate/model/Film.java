package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;

@Data
@AllArgsConstructor
public class Film {
  private Long id;

  @NotBlank(message = "Не может быть пустым")
  private String name;

  @Size(min = 1, max = 200, message = "Максимальная длинна 200 символов")
  private String description;

  @ValidReleaseDate private LocalDate releaseDate;

  @Positive(message = "Продолжительность фильма должна быть положительной")
  private long duration;

  private Set<Long> likes;

  private Mpa mpa;

  private Set<Genre> genres;
}
