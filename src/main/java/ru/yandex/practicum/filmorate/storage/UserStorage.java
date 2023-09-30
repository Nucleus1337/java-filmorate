package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
  User add(User user);

  User update(User user);

  void remove(Long id);

  List<User> findAll();

  User findById(Long id);
}
