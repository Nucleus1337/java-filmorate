package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Mpa;

public interface MpaDao {
    List<Mpa> findAll();

    Mpa findById(Long id);
}
