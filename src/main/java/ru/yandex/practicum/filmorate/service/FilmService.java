package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
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
            return null;
        }
    }

    public Optional<Film> updateFilm(@NotNull Film updatedFilm) throws JsonProcessingException {
        if (filmDbStorage.checkIsObjectInStorage(updatedFilm.getId())) {
            if (FilmValidation.isFilmValid(updatedFilm)) {
                return filmDbStorage.update(updatedFilm);
            } else {
                return Optional.empty();
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
        return filmDbStorage.getAll().stream()
                .sorted((f1, f2) -> (f1.getSetOfLikes().size() - f2.getSetOfLikes().size()) * (-1))
                .limit(count)
                .collect(Collectors.toList());
    }
}