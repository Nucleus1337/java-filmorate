package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserSequence;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/users")
@Slf4j
public class UserController {
    private final Map<Long, User> idToUser = new HashMap<>();

    @GetMapping
    public Map<Long, User> findAll() {
        log.info("Возвращаем всех пользователей");

        if (idToUser.size() == 0) return Collections.emptyMap();

        return idToUser;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        long id = UserSequence.getNextId();

        if (user.getId() == null) {
            user.setId(id);
        } else if (idToUser.containsKey(user.getId())) {
            throw new UserUpdateException(String.format("Пользователь с id=%s уже существует", user.getId()));
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        log.info("Сохраняем нового пользователя {}", user);
        idToUser.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (idToUser.containsKey(user.getId())) {
            log.info("Обновляем данные пользователя с id={}", user.getId());
            idToUser.put(user.getId(), user);
        } else {
            log.warn("Пользователь с id={} не существует", user.getId());
            throw new UserUpdateException(String.format("Пользователя с id=%s не существует", user.getId()));
        }

        return user;
    }

    class UserUpdateException extends RuntimeException {
        public UserUpdateException(String error) {
            super(error);
        }
    }
}
