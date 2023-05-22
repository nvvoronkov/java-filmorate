package ru.yandex.practicum.filmorate.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    private static int userId = 0;
    private final Map<Integer, User> userMap = new HashMap<>();

    public Integer generateId() {
        return ++userId;
    }

    @PostMapping
    private User addNewUser(@Valid @RequestBody User user) {
        for (User valueComparison : userMap.values()) {
            if (valueComparison.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Фильм уже есть в нашей базе");
            }
        }
        user.setId(generateId());
        if (user.getLogin().contains(" ")) {
            log.warn(user.getLogin());
            throw new RuntimeException("Логин не может быть пустым");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userMap.put(user.getId(), user);
        log.trace("Добавлен пользователь {} ", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult)
            throws JsonProcessingException {
        int id = user.getId();
        if (id == 0) {
            log.warn("Отсутствует id");
            throw new ValidationException("Отсутствует id");
        } else if (!userMap.containsKey(id)) {
            log.warn("Пользователь с id {} не найден", id);
            throw new ValidationException("Пользователь с id " + id + " не найден");
        } else {
            userMap.put(id, user);
            log.debug("Пользователь {} обновлен", userMap.get(id));
        }
        return user;
    }

    @GetMapping
    private List<User> getListOfUsers() {
        log.debug("Текущее количество пользователей: {}", userMap.size());
        return new ArrayList<>(userMap.values());
    }
}
