package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenresStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenresStorage genresStorage;

    @Autowired
    public GenreService(GenresStorage genresStorage) {
        this.genresStorage = genresStorage;
    }

    public Genre getGenreById(Integer genreId) {
        if (genresStorage.getGenreById(genreId) == null) {
            log.info("Жанр с id " + genreId + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return genresStorage.getGenreById(genreId);
    }

    public List<Genre> getAllGenre() {
        return genresStorage.getAllGenres();
    }
}
