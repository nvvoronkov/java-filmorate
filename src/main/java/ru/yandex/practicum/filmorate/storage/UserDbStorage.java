package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserDbStorage implements Storage<User> {
    private final static Integer REQUEST_TO_FRIENDS_STATUS = 1;
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT user_id, " +
                "user_name, " +
                "email, " +
                "login, " +
                "birthday " +
                "FROM users " +
                "ORDER BY user_id";
        return jdbcTemplate.query(sqlQuery, userMapper);
    }

    @Override
    public Optional<User> getById(Integer userId) {
        if (checkIsObjectInStorage(userId)) {
            String sqlQuery = "SELECT user_id, " +
                    "user_name, " +
                    "email, " +
                    "login, " +
                    "birthday " +
                    "FROM users " +
                    "WHERE user_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, userMapper, userId));
        } else {
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }

    @Override
    public User add(User newUser) {
        String sqlQuery = "INSERT INTO users (user_name, login, email, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, newUser.getName());
            stmt.setString(2, newUser.getLogin());
            stmt.setString(3, newUser.getEmail());
            stmt.setString(4, newUser.getBirthday().toString());
            return stmt;
        }, keyHolder);
        newUser.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return newUser;
    }

    public void addFriend(int userId, int friendId) {
        boolean isUserExist = checkIsObjectInStorage(userId);
        boolean isFriendExist = checkIsObjectInStorage(friendId);
        if (isUserExist && isFriendExist) {
            String sqlQuery = "INSERT INTO users_friends (user_id, friend_id, status) " +
                    "VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId, REQUEST_TO_FRIENDS_STATUS);
        } else {
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.",
                    isUserExist ? friendId : userId));
        }
    }

    @Override
    public Optional<User> update(User updatedUser) {
        if (checkIsObjectInStorage(updatedUser)) {
            String sqlQuery = "UPDATE users " +
                    "SET user_name = ?, email = ?, login = ?, birthday = ? " +
                    "WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery
                    , updatedUser.getName()
                    , updatedUser.getEmail()
                    , updatedUser.getLogin()
                    , updatedUser.getBirthday()
                    , updatedUser.getId());
            return Optional.of(updatedUser);
        } else {
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", updatedUser.getId()));
        }
    }

    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM users_friends " +
                "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public List<User> getListOfFriends(int userId) {
        if (checkIsObjectInStorage(userId)) {
            String sqlQuery = "SELECT u.user_id, " +
                    "u.user_name, " +
                    "u.email, " +
                    "u.login, " +
                    "u.birthday " +
                    "FROM users_friends AS uf LEFT JOIN users AS u " +
                    "ON uf.friend_id = u.user_id " +
                    "WHERE uf.user_id = ?" +
                    "ORDER BY u.user_id";
            return jdbcTemplate.query(sqlQuery, userMapper, userId);
        } else {
            throw new ObjectNotFoundException(String.format("Пользователь id=%s не найден.", userId));
        }
    }

    public List<User> getListOfCommonFriends(int userId, int friendId) {
        String sqlQuery = "WITH friends AS " +
                "( SELECT user_id, friend_id " +
                "FROM users_friends AS uf " +
                "WHERE uf.status IS NOT NULL ) " +
                "SELECT u.user_id, " +
                "u.user_name, " +
                "u.email, " +
                "u.login, " +
                "u.birthday " +
                "FROM users u JOIN friends f1 " +
                "ON u.user_id = f1.friend_id " +
                "JOIN friends f2 " +
                "ON f1.friend_id = f2.friend_id " +
                "AND f1.friend_id <> f2.user_id " +
                "AND f2.friend_id <> f1.user_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sqlQuery, userMapper, userId, friendId);
    }

    public boolean checkIsObjectInStorage(User user) {
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM users WHERE user_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, user.getId()));
    }

    public boolean checkIsObjectInStorage(int userId) {
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM users WHERE user_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, userId));
    }

    //TODO
    public boolean areTheseUsersFriends(int userId, int friendId) {
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM users_friends WHERE user_id = ? AND friend_id = ?)";
        return !Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, userId));
    }
}
