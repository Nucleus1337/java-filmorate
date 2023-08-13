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
@RequestMapping(value = "/api/v1/user")
@Slf4j
public class UserController {
    private final Map<Long, User> idToUser = new HashMap<>();

    @GetMapping
    public Map<Long, User> findAll() {
        log.info("Возвращаем всех пользователей");
        return idToUser;
    }

    @PostMapping
    @PutMapping
    public User add(@Valid @RequestBody User user) {
        if (idToUser.containsKey(user.getId())) {
            log.info("Обновляем данные пользователя с id={}", user.getId());
        } else {
            log.info("Сохраняем нового пользователя {}", user);
        }

        idToUser.put(user.getId(), user);

        return user;
    }
}
