package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidation;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    @Autowired
    public final InMemoryFilmStorage filmStorage;
    public final InMemoryUserStorage userStorage;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getListOfFilms() {
        return filmStorage.getListOfFilms();
    }

    public Film getFilmById(int filmId) {
        if (filmStorage.isFilmInStorage(filmId)) {
            return filmStorage.getFilmById(filmId);
        } else {
            log.info("Фильм id={} не найден", filmId);
            throw new NotFoundException(String.format("Фильм id=%s не найден", filmId));
        }
    }

    public Film addNewFilm(Film newFilm) throws JsonProcessingException {
        if (FilmValidation.isFilmValid(newFilm)) {
            return filmStorage.addNewFilm(newFilm);
        } else {
            return null;
        }
    }

    public Film updateFilm(Film updatedFilm) throws JsonProcessingException {
        if (filmStorage.isFilmInStorage(updatedFilm) && FilmValidation.isFilmValid(updatedFilm)) {
            if (filmStorage.isFilmInStorage(updatedFilm.getId())) {
                return filmStorage.updateFilm(updatedFilm);
            } else {
                return null;
            }
        } else {
            log.info("Фильм id={} не найден", updatedFilm);
            throw new NotFoundException(String
                    .format("Не удалось обновить данные о фильме id=%s т.к. фильм  не найден", updatedFilm.getId()));
        }
    }

    public void addLike(int filmId, int userId) {
        if (!filmStorage.isFilmInStorage(filmId)) {
            log.info("Фильм id={} не найден", filmId);
            throw new NotFoundException(String.format("Фильм id=%s не найден", userId));
        }
        if (!userStorage.isUserInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (!filmStorage.isFilmInStorage(filmId)) {
            log.info("Фильм id={} не найден", filmId);
            throw new NotFoundException(String.format("Фильм id=%s не найден", filmId));
        }
        if (!userStorage.isUserInStorage(userId)) {
            log.info("Пользователь id={} не найден", userId);
            throw new NotFoundException(String.format("Пользователь id=%s не найден", userId));
        }
        if (!filmStorage.isFilmHasLikeFromUser(filmId, userId)) {
            log.info("Для фильма id={} лайк от пользователя id={} не найден", filmId, userId);
            throw new ValidationException(String.format("Для фильма id=%s лайк от пользователя id=%s не найден",
                    filmId, userId));
        }
        filmStorage.deleteLike(filmId, userId);
    }

    public void deleteFilm(int filmId) {
        if (!filmStorage.isFilmInStorage(filmId)) {
            log.info("Фильм id={} не найден", filmId);
            throw new NotFoundException(String.format("Фильм id=%s не найден", filmId));
        }
        filmStorage.deleteFilm(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Направлен список из {} фильмов с наибольшим количеством лайков", count);
        return filmStorage.getListOfFilms().stream()
                .sorted((f1, f2) -> (f1.getSetOfLikes().size() - f2.getSetOfLikes().size()) * (-1))
                .limit(count)
                .collect(Collectors.toList());
    }
}