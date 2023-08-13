package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> idToFilms = new HashMap<>();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) throws FilmIsAlreadyExistsException {
        if (idToFilms.containsKey(film.getId())) {
            log.error(String.format("Фильм с id=%s уже существует", film.getId()));
            throw new FilmIsAlreadyExistsException(String.format("Фильм с id=%s уже существует", film.getId()));
        }

        idToFilms.put(film.getId(), film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws FilmIsAlreadyExistsException {
        if (idToFilms.containsKey(film.getId())) {
            idToFilms.put(film.getId(), film);
        } else {
            log.error(String.format("Фильм с id=%s не существует", film.getId()));
            throw new FilmIsAlreadyExistsException(String.format("Фильм с id=%s не существует", film.getId()));
        }

        return film;
    }

    @GetMapping
    public Map<Long, Film> findAll() {
        return idToFilms;
    }

    class FilmIsAlreadyExistsException extends Exception {
        public FilmIsAlreadyExistsException(String error) {
            super(error);
        }
    }
}
