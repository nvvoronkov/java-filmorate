package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

public interface FilmStorage extends Storage<Film> {
    Film add(Film film);

    void delete(Integer filmId);

    void deleteLike(int filmId, int userid);

    Optional<Film> update(Film film);

    Optional<Film> getById(Integer filmId);

    void addLike(int filmId, int userId);

    boolean isFilmInStorage(int filmId);

    boolean isFilmInStorage(Film film);

    boolean isFilmHasLikeFromUser(int filmId, int userId);
}