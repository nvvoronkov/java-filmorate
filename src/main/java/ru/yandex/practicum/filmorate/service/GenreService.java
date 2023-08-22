package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> getAll() {
        return genreDbStorage.getAll();
    }

    public Optional<Genre> getById(int genreId) {
        if (genreDbStorage.checkIsObjectInStorage(genreId)) {
            return genreDbStorage.getById(genreId);
        } else {
            log.info("Жанр id={} не найден", genreId);
            throw new NotFoundException(String.format("Жанр id=%s не найден", genreId));
        }
    }
}
