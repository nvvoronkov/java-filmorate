package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresStorage {
    Genre getGenreById(Integer genreId);

    List<Genre> getAllGenres();

    void createGenre(Genre genre);
}
