package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmSequence;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> idToFilms = new HashMap<>();
    private final List<Film> films = new ArrayList<>();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        long id = FilmSequence.getNextId();

        if (film.getId() == null) {
            film.setId(id);
        } else if (idToFilms.containsKey(film.getId())) {
            log.error(String.format("Фильм с id=%s уже существует", film.getId()));
            throw new FilmIsAlreadyExistsException(String.format("Пользователь с id=%s уже существует", film.getId()));
        }

        idToFilms.put(film.getId(), film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (idToFilms.containsKey(film.getId())) {
            idToFilms.put(film.getId(), film);
        } else {
            log.error(String.format("Фильм с id=%s не существует", film.getId()));
            throw new FilmIsAlreadyExistsException(String.format("Фильм с id=%s не существует", film.getId()));
        }

        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Возвращаем всех пользователей");

        if (idToFilms.size() == 0) return Collections.emptyList();

        return new ArrayList<>(idToFilms.values());
    }

    class FilmIsAlreadyExistsException extends RuntimeException {
        public FilmIsAlreadyExistsException(String error) {
            super(error);
        }
    }
}
