package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

public interface UserStorage extends Storage<User> {
    List<User> getListOfFriends(int userId);

    void addNewFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    boolean isUserInStorage(User user);

    boolean isUserInStorage(int userId);

    boolean areTheseUsersFriends(Integer userId, Integer friendId);
}
