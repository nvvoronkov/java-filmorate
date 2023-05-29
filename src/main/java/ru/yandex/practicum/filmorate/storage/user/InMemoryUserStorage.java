package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    private final Map<Integer, User> users = new HashMap<>();

    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User addNewUser(User user) {
        user.generateAndSetId();
        user.generateSetOfFriends();
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь id={}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        updatedUser.setSetOfFriends(users.get(updatedUser.getId()).getSetOfFriends());
        users.put(updatedUser.getId(), updatedUser);
        log.info("Данные пользователя id = {} обновлены", updatedUser.getId());
        return updatedUser;
    }

    @Override
    public List<User> getListOfUsers() {
        log.info("Направлен список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int userId) {
        log.info("Направлен пользователь id={}", userId);
        return users.get(userId);
    }

    @Override
    public void addNewFriend(int userId, int friendId) {
        users.get(userId).addFriend(friendId);
        log.info("Пользователю id={} добавлен новый друг id={}", userId, friendId);
        users.get(friendId).addFriend(userId);
        log.info("Пользователю id={} добавлен новый друг id={}", friendId, userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        users.get(userId).deleteFriend(friendId);
        log.info("Из списка друзей пользователя id={} удвлен друг id={}", userId, friendId);
        users.get(friendId).deleteFriend(userId);
        log.info("Из списка друзей пользователя id={} удвлен друг id={}", friendId, userId);
    }

    @Override
    public void deleteUser(int userId) {
        users.remove(userId);
        log.info("Пользователя с id={} удален", userId);
    }

    @Override
    public List<User> getListOfFriends(int userId) {
        log.info("Направлен список друзей пользователя id={}", userId);
        return users.get(userId).getSetOfFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public boolean isUserInStorage(User user) {
        return users.containsValue(user);
    }

    public boolean isUserInStorage(int userId) {
        return users.containsKey(userId);
    }

    public boolean areTheseUsersFriends(Integer userId, Integer friendId) {
        return (users.get(userId).getSetOfFriends().contains(friendId));
    }

    public static boolean isUserValid(User user) throws JsonProcessingException {
        return isLoginValid(user) && isBirthdayValid(user);
    }

    private static boolean isBirthdayValid(User user) throws JsonProcessingException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения {} не может быть в будущем", mapper.writeValueAsString(user.getBirthday()));
            throw new ValidationException("Дата рождения " + user.getBirthday() + "не может быть из будущего");
        }
        return true;
    }

    private static boolean isLoginValid(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин {} содержит пробелы", user.getLogin());
            throw new ValidationException("Логин " + user.getLogin() + " содержит пробелы");
        }
        return true;
    }
}