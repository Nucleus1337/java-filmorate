package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
  public Film add(Film film);

  public Film update(Film film);

  public void remove(Long id);

  public List<Film> findAll();

  public Film findById(Long id);
}
