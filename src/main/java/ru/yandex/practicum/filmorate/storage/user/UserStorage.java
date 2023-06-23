package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User addNewUser(User user);

    User getUserById(int userId);

    void deleteUser(int userId);

    User updateUser(User user);

    List<User> getListOfUsers();

    List<User> getListOfFriends(int userId);

    void addNewFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    boolean isUserInStorage(User user);

    boolean isUserInStorage(int userId);

    boolean areTheseUsersFriends(Integer userId, Integer friendId);
}
