package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
        return idToUser;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.info("Сохраняем нового пользователя {}", user);
        idToUser.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновляем данные пользователя с id={}", user.getId());

        if (idToUser.containsKey(user.getId())) {
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
