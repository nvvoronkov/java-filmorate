package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getListOfUsers() {
        return userStorage.getListOfUsers();
    }

    public User getUserById(int userId) {
        if (userStorage.isUserInStorage(userId)) {
            return userStorage.getUserById(userId);
        } else {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
    }

    public User addNewUser(User user) throws JsonProcessingException  {
        if (UserValidation.isUserValid(user)) {
            return userStorage.addNewUser(user);
        } else
            return null;
    }

    public User updateUser(User updatedUser) throws JsonProcessingException {
        if (userStorage.isUserInStorage(updatedUser.getId())) {
            if (UserValidation.isUserValid(updatedUser)) {
                return userStorage.updateUser(updatedUser);
            } else
                return null;
        } else {
            log.info("Пользователь id={} не найден", updatedUser.getId());
            throw new NotFoundException(String.format("Не удалось обновить данные пользователя id=%s т.к. " +
                    "пользователь не найден", updatedUser.getId()));
        }
    }

    public void addNewFriend(int userId, int friendId) {
        if (!(userStorage.isUserInStorage(userId))) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        if (!userStorage.isUserInStorage(friendId)) {
            log.info("Пользователь id={} не найден", friendId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", friendId));
        }
        userStorage.addNewFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        if (!userStorage.isUserInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        if (!userStorage.isUserInStorage(friendId)) {
            log.info("Пользователь id={} не найден", friendId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", friendId));
        }
        if (!userStorage.areTheseUsersFriends(userId, friendId)) {
            log.info("Пользователь id={} не в списке друзей пользователя id={}", friendId, userId);
            throw new NotFoundException(String.format("Пользователь id=%s не в списке друзей пользователя id=%s",
                    friendId, userId));
        }
        if (!userStorage.areTheseUsersFriends(friendId, userId)) {
            log.info("Пользователь id={} не в списке друзей пользователя id={}", userId, friendId);
            throw new NotFoundException(String.format("Пользователь id=%s не в списке друзей пользователя id=%s",
                    userId, friendId));
        }
        userStorage.deleteFriend(userId, friendId);
    }

    public void deleteUser(int userId) {
        if (!userStorage.isUserInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        userStorage.deleteUser(userId);
    }

    public List<User> getListOfFriends(int userId) {
        if (!userStorage.isUserInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        return userStorage.getListOfFriends(userId);
    }

    public List<User> getListOfCommonFriends(int userId, int friendId) {
        if (!userStorage.isUserInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        if (!userStorage.isUserInStorage(friendId)) {
            log.info("Пользователь id={} не найден", friendId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", friendId));
        }
        ArrayList<User> resultList = new ArrayList<>(userStorage.getListOfFriends(userId));
        resultList.retainAll(userStorage.getListOfFriends(friendId));
        log.info("Направлен общий список друзей пользователей id={} и id={}", userId, friendId);
        return resultList;
    }

}