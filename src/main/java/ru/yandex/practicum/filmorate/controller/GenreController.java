package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

@RestController
@RequestMapping(value = "/genres")
@RequiredArgsConstructor
public class GenreController {
  private final GenreService genreService;

  @GetMapping
  public List<Genre> findAll() {
    return genreService.findAll();
  }

  @GetMapping("/{id}")
  public Genre findById(@PathVariable Long id) {
    return genreService.findById(id);
  }
}
