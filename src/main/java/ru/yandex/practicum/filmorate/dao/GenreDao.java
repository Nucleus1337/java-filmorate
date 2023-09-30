package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreDao {
    List<Genre> findAll();

    Genre findById(Long id);
}
