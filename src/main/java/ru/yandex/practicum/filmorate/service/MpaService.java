package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

@Service
@RequiredArgsConstructor
public class MpaService {
  private final MpaDao mpaDao;

  public List<Mpa> findAll() {
    return mpaDao.findAll();
  }

  public Mpa findById(Long id) {
    return mpaDao.findById(id);
  }
}
