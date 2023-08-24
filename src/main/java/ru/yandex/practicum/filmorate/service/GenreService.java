package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public List<Genre> getAll() {
        return genreDbStorage.getAll();
    }

    public Genre getById(int genreId) {
        if (genreDbStorage.checkIsObjectInStorage(genreId)) {
            return genreDbStorage.getById(genreId);
        } else {
            log.info("Жанр id={} не найден", genreId);
            throw new NotFoundException(String.format("Жанр id=%s не найден", genreId));
        }
    }
}
