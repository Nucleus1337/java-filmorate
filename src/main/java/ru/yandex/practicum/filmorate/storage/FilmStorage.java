package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
  Film add(Film film);

  Film update(Film film);

  void remove(Long id);

  List<Film> findAll();

  Film findById(Long id);

  long getNextId();
}
