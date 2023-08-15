package ru.yandex.practicum.filmorate.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;

@Slf4j
public class GenreValidation {
    public static boolean isGenreValid(Genre genre) {
        boolean result = false;
        for (String genres : genre.getGenres()) {
            if (genres.equals(genre.toString().toLowerCase())) {
                result = true;
            }
        }
        return result;
    }
}
