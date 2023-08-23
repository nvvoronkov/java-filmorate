package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidation;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage,
                       UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public List<Film> getListOfFilms() {
        return filmDbStorage.getAll();
    }

    public Optional<Film> getFilmById(int filmId) {
        if (filmDbStorage.checkIsObjectInStorage(filmId)) {
            return filmDbStorage.getById(filmId);
        } else {
            log.info("Фильм id={} не найден", filmId);
            throw new NotFoundException(String.format("Фильм id=%s не найден", filmId));
        }
    }

    public Film addNewFilm(Film newFilm) throws JsonProcessingException {
        if (FilmValidation.isFilmValid(newFilm)) {
            return filmDbStorage.add(newFilm);
        } else {
            throw new ValidationException(String.format("Фильм id=%s не прошел валидацию", newFilm.getId()));
        }
    }

    public Optional<Film> updateFilm(@NotNull Film updatedFilm) throws JsonProcessingException {
        if (filmDbStorage.checkIsObjectInStorage(updatedFilm.getId())) {
            if (FilmValidation.isFilmValid(updatedFilm)) {
                return filmDbStorage.update(updatedFilm);
            } else {
                throw new ValidationException("Фильм не прошёл валидацию");
            }
        } else {
            log.info("Фильм id={} не найден", updatedFilm);
            throw new NotFoundException(String
                    .format("Не удалось обновить данные о фильме id=%s т.к. фильм  не найден", updatedFilm.getId()));
        }
    }

    public void addLike(int filmId, int userId) {
        if (!filmDbStorage.checkIsObjectInStorage(filmId)) {
            log.info("Фильм id={} не найден", filmId);
            throw new NotFoundException(String.format("Фильм id=%s не найден", userId));
        }
        if (!userDbStorage.checkIsObjectInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        filmDbStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (!filmDbStorage.checkIsObjectInStorage(filmId)) {
            log.info("Фильм id={} не найден", filmId);
            throw new NotFoundException(String.format("Фильм id=%s не найден", filmId));
        }
        if (!userDbStorage.checkIsObjectInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        if (!filmDbStorage.checkIsFilmHasLikeFromUser(filmId, userId)) {
            log.info("Для фильма id={} лайк от пользователя id={} не найден", filmId, userId);
            throw new ValidationException(String.format("Для фильма id=%s лайк от пользователя id=%s не найден",
                    filmId, userId));
        }
        filmDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Направлен список из {} фильмов с наибольшим количеством лайков", count);
        return filmDbStorage.getPopularFilms(count);
    }
}