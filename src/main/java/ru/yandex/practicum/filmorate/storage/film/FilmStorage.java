package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film addNewFilm(Film film);

    void deleteFilm(int filmId);

    void deleteLike(int filmId, int userid);

    Film updateFilm(Film film);

    List<Film> getListOfFilms();

    Film getFilmById(int filmId);

    void addLike (int filmId, int userId);
}