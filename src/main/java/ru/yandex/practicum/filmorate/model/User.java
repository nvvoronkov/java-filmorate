package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    static int identificator = 0;
    int id;
    @Email(message = "Указан некорректный email")
    @NotBlank
    String email;
    @NotBlank(message = "Не указан login")
    String login;
    String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
    Set<Integer> setOfFriends;

    public void generateAndSetId() {
        setId(++identificator);
    }

    public void generateSetOfFriends() {
        this.setOfFriends = new HashSet<>();
    }

    public void addFriend(int friendId) {
        setOfFriends.add(friendId);
    }

    public void deleteFriend(int friend) {
        setOfFriends.remove(friend);
    }

    public void setName(String name) {
        this.name = name;
    }
}