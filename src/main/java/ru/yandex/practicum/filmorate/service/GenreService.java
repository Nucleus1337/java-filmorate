package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

@Service
@AllArgsConstructor
public class GenreService {
  private final GenreDao genreDao;

  public List<Genre> findAll() {
    return genreDao.findAll();
  }

  public Genre findById(Long id) {
    return genreDao.findById(id);
  }
}
