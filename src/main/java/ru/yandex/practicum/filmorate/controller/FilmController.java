package ru.yandex.practicum.filmorate.controller;

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
@RequestMapping(value = "/api/v1/film")
public class FilmController {
    private final Map<Long, Film> idToFilms = new HashMap<>();

    @PostMapping
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        idToFilms.put(film.getId(), film);

        return film;
    }

    @GetMapping
    public Map<Long, Film> findAll() {
        return idToFilms;
    }
}
