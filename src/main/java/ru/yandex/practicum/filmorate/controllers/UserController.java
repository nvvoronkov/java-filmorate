package ru.yandex.practicum.filmorate.controllers;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@RequestMapping("/users")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    private User addNewUser(@Valid @RequestBody User user) throws JsonProcessingException {
        log.info("Получен запрос на добавление нового пользователя");
        return userService.addNewUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) throws JsonProcessingException {
        log.info("Получен запрос на обновление данных пользователя id={}", updatedUser.getId());
        return userService.updateUser(updatedUser);
    }

    @GetMapping
    private List<User> getListOfUsers() {
        log.info("Получен запрос на получение списка пользователей");
        return userService.getListOfUsers();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") int userId) {
        log.info("Получен запрос на получение пользователя id={}", userId);
        return userService.getUserById(userId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.info("Получен запрос на добавление польхователя id={} в друзья пользователю id={}", friendId, userId);
        userService.addNewFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        log.info("Получен запрос на удаление польхователя id={} из друзей пользователя id={}", 
                friendId, userId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{id}/friends")
    @ResponseBody
    public List<User> getListOfFriends(@PathVariable("id") int userId) {
        log.info("Получен запрос на получения списка друзей пользователя id={}", userId);
        return userService.getListOfFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    List<User> getListOfCommonFriends(@PathVariable("id") int userId, @PathVariable("otherId") int friendId) {
        log.info("Получен запрос на получение общего списка друзей пользователей id={} и id={}", 
                userId, friendId);
        return userService.getListOfCommonFriends(userId, friendId);
    }
}