package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/films")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FilmController {
    final FilmService filmService;

    @PostMapping
    public Film addNewFilm(@RequestBody Film newFilm) throws JsonProcessingException {
        log.info("Получен запрос на добавление нового фильма");
        return filmService.addNewFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws JsonProcessingException {
        log.info("Получен запрос на обновление фильма id={}", film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") int filmId) {
        log.info("Получен запрос на получение фильма id={}", filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping
    public List<Film> getListOfFilms() {
        log.info("Получен запрос на получение списка фильмов");
        return filmService.getListOfFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Получен запрос на добавление лайка фильму id={} от пользователя id={}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("Получен запрос на удаление лайка фильму id={} от пользователя id={}", filmId, userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10")
                                      @Positive(message = "Количество фиильмов в списке должно быть положительным")
                                      int count) {
        log.info("Получен запрос на получение списка из {} фильмов с наибольшим количеством лайков", count);
        return filmService.getPopularFilms(count);
    }
}