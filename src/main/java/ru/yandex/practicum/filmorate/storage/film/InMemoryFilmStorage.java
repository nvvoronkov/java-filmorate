package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    public static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public Film add(Film newFilm) {
        newFilm.generateAndSetId();
        newFilm.generateSetOfLikes();
        films.put(newFilm.getId(), newFilm);
        log.info("Добавлен новый фильм id={}", newFilm.getId());
        return newFilm;
    }

    @Override
    public Optional<Film> update(Film updatedFilm) {
        updatedFilm.setSetOfLikes(films.get(updatedFilm.getId()).getSetOfLikes());
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Данные о фильме id = {} обновлены", updatedFilm.getId());
        return Optional.of(updatedFilm);
    }

    @Override
    public List<Film> getAll() {
        log.info("Направлен список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getById(Integer filmId) {
        log.info("Направлен фильм id={}", filmId);
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public void addLike(int filmId, int userId) {
        films.get(filmId).addLike(userId);
        log.info("Фильму id = {} добавлен лайк пользователя id={}", filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        films.get(filmId).deleteLike(userId);
        log.info("Для фильма id = {} удален лайк пользователя id={}", filmId, userId);
    }

    @Override
    public void delete(Integer filmId) {
        films.remove(filmId);
        log.info("Фильм с id = {} удален.", filmId);
    }

    public boolean isFilmInStorage(Film film) {
        return films.containsValue(film);
    }

    @Override
    public boolean isFilmInStorage(int filmId) {
        return films.containsKey(filmId);
    }

    @Override
    public boolean isFilmHasLikeFromUser(int filmId, int userId) {
        return films.get(filmId).getSetOfLikes().contains(userId);
    }
}