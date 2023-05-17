package ru.yandex.practicum.filmorate.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@RestController
@Slf4j
@RequestMapping("/films")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmController {
    static final LocalDate START_DATE = LocalDate.of(1985, 12, 28);
    final Map<Integer, Film> filmMap = new HashMap<>();
    int filmId = 0;

    public Integer generateId() {
        return ++filmId;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film newFilm) {
        for (Film entry : filmMap.values()) {
            if (entry.getName().equals(newFilm.getName()) && isValidDate(newFilm)) {
                throw new ValidationException("Фильм уже есть в нашей базе");
            }
        }
        newFilm.setId(generateId());
        filmMap.put(newFilm.getId(), newFilm);
        log.trace("Добавален новый фильм {}", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        int id = film.getId();
        if (filmMap.containsKey(id) && isValidDate(film)) {
            filmMap.put(id, film);
            log.trace("Обновлен фильм: {}", film);
            return film;
        } else {
            throw new ValidationException("Фильма с id: " + id + " нет.");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    private boolean isValidDate(Film film) {
        if (film.getReleaseDate().isBefore(START_DATE)) {
            log.warn(film.getReleaseDate().toString());
            throw new ValidationException("Дата выхода фильма не может быть раньше " + START_DATE);
        }
        for (Film valueComparison : filmMap.values()) {
            if (valueComparison.getName().equals(film.getName())) {
                throw new ValidationException("Фильм уже есть в нашей базе");
            }
        }
        return true;
    }
}
