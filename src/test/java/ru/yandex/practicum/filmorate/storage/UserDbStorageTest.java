package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:TestUserData.sql")
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    public void shouldGetAllUsers() {
        List<User> userList = userDbStorage.getAll();
        assertThatList(userList).hasSizeBetween(4, 4);
    }

    @Test
    public void shouldGetListOfFriends() {
        List<User> userList = userDbStorage.getAll();
        User testUser = userList.get(0);
        User testFriend1 = userList.get(1);
        User testFriend2 = userList.get(2);
        userDbStorage.addFriend(testUser.getId(), testFriend1.getId());
        userDbStorage.addFriend(testUser.getId(), testFriend2.getId());
        List<User> friends = userDbStorage.getListOfFriends(testUser.getId());
        assertThat(friends.get(0)).isEqualTo(testFriend1);
        assertThat(friends.get(1)).isEqualTo(testFriend2);
    }

    @Test
    public void shouldAddFriend() {
        List<User> userList = userDbStorage.getAll();
        User testUser = userList.get(0);
        User testFriend1 = userList.get(1);
        userDbStorage.addFriend(testUser.getId(), testFriend1.getId());
        List<User> friends = userDbStorage.getListOfFriends(testUser.getId());
        assertThat(friends.get(0)).isEqualTo(testFriend1);
    }

    @Test
    public void shouldDeleteFriend() {
        List<User> userList = userDbStorage.getAll();
        User testUser = userList.get(0);
        User testFriend1 = userList.get(1);
        userDbStorage.addFriend(testUser.getId(), testFriend1.getId());
        List<User> friends = userDbStorage.getListOfFriends(testUser.getId());
        assertThat(friends.get(0)).isEqualTo(testFriend1);
        userDbStorage.deleteFriend(testUser.getId(), testFriend1.getId());
        List<User> friendsAfterDelete = userDbStorage.getListOfFriends(testUser.getId());
        assertThatList(friendsAfterDelete).hasSizeBetween(0, 0);
    }

    @Test
    public void shouldGetListOfCommonFriends() {
        List<User> userList = userDbStorage.getAll();
        User testUser1 = userList.get(0);
        User testUser2 = userList.get(1);
        User testFriend1 = userList.get(2);
        User testFriend2 = userList.get(3);
        userDbStorage.addFriend(testUser1.getId(), testFriend1.getId());
        userDbStorage.addFriend(testUser1.getId(), testFriend2.getId());
        userDbStorage.addFriend(testUser2.getId(), testFriend1.getId());
        userDbStorage.addFriend(testUser2.getId(), testFriend2.getId());
        List<User> commonFriends = userDbStorage.getListOfCommonFriends(testUser1.getId(), testUser2.getId());
        assertThat(commonFriends.get(0)).isEqualTo(testFriend1);
        assertThat(commonFriends.get(1)).isEqualTo(testFriend2);
    }
}