package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userDbStorage;

    public List<User> getListOfUsers() {
        return (List<User>) userDbStorage.getAll();
    }

    public User getUserById(int userId) {
        if (userDbStorage.checkIsObjectInStorage(userId)) {
            return userDbStorage.getById(userId);
        } else {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
    }

    public User addNewUser(User user) throws JsonProcessingException {
        if (UserValidation.isUserValid(user)) {
            return userDbStorage.add(user);
        } else
            return null;
    }

    public User updateUser(User updatedUser) throws JsonProcessingException {
        if (userDbStorage.checkIsObjectInStorage(updatedUser.getId())) {
            if (UserValidation.isUserValid(updatedUser)) {
                return userDbStorage.update(updatedUser);
            } else
                return null;
        } else {
            log.info("Пользователь id={} не найден", updatedUser.getId());
            throw new NotFoundException(String.format("Не удалось обновить данные пользователя id=%s т.к. " +
                    "пользователь не найден", updatedUser.getId()));
        }
    }

    public void addNewFriend(int userId, int friendId) {
        if (!(userDbStorage.checkIsObjectInStorage(userId))) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        if (!userDbStorage.checkIsObjectInStorage(friendId)) {
            log.info("Пользователь id={} не найден", friendId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", friendId));
        }
        userDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        if (!userDbStorage.checkIsObjectInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        if (!userDbStorage.checkIsObjectInStorage(friendId)) {
            log.info("Пользователь id={} не найден", friendId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", friendId));
        }
        if (userDbStorage.areTheseUsersFriends(userId, friendId)) {
            log.info("Пользователь id={} не в списке друзей пользователя id={}", friendId, userId);
            throw new NotFoundException(String.format("Пользователь id=%s не в списке друзей пользователя id=%s",
                    friendId, userId));
        }
        userDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getListOfFriends(int userId) {
        if (!userDbStorage.checkIsObjectInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        return userDbStorage.getListOfFriends(userId);
    }

    public List<User> getListOfCommonFriends(int userId, int friendId) {
        if (!userDbStorage.checkIsObjectInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        if (!userDbStorage.checkIsObjectInStorage(friendId)) {
            log.info("Пользователь id={} не найден", friendId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", friendId));
        }
        ArrayList<User> resultList = new ArrayList<>(userDbStorage.getListOfFriends(userId));
        resultList.retainAll(userDbStorage.getListOfFriends(friendId));
        log.info("Направлен общий список друзей пользователей id={} и id={}", userId, friendId);
        return resultList;
    }

}