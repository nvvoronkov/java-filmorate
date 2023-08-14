package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User add(User user) {
        user.generateAndSetId();
        user.generateSetOfFriends();
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь id={}", user.getId());
        return user;
    }

    @Override
    public Optional<User> update(User updatedUser) {
        updatedUser.setSetOfFriends(users.get(updatedUser.getId()).getSetOfFriends());
        users.put(updatedUser.getId(), updatedUser);
        log.info("Данные пользователя id = {} обновлены", updatedUser.getId());
        return Optional.of(updatedUser);
    }

    @Override
    public List<User> getAll() {
        log.info("Направлен список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(Integer userId) {
        log.info("Направлен пользователь id={}", userId);
        return Optional.ofNullable(users.get(userId));
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
    public void delete(Integer userId) {
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
}