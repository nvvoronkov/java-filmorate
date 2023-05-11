package ru.yandex.practicum.filmorate.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    static int userId;
    final Map<Integer, User> userMap;

    public UserController() {
        userMap = new HashMap<>();
        userId = 0;
    }

    public Integer generateId() {
        return ++userId;
    }

    @PostMapping
    private User addNewUser(@Validated @RequestBody User user) {
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
    private void updateUser() {

    }

    @GetMapping
    private List<User> getListOfUsers() {
        return null;

    }
}
