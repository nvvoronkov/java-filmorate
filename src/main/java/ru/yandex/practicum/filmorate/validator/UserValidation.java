package ru.yandex.practicum.filmorate.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidation {
    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

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

    public static boolean isUserValid(User user) throws JsonProcessingException {
        return isLoginValid(user) && isBirthdayValid(user) && isUserNameValid(user);
    }

    public static boolean isUserNameValid(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;
    }
}