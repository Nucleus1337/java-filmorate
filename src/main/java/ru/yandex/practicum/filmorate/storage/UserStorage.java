package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
  public User add(User user);

  public User update(User user);

  public void remove(Long id);

  public List<User> findAll();

  public User findById(Long id);
}
