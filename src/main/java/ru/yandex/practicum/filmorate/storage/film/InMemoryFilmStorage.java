package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    public static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public Map<Integer, Film> getFilms() {
        return films;
    }
    
    @Override
    public Film addNewFilm(Film newFilm) {
        newFilm.generateAndSetId();
        newFilm.generateSetOfLikes();
        films.put(newFilm.getId(), newFilm);
        log.info("Добавлен новый фильм id={}", newFilm.getId());
        return newFilm;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        updatedFilm.setSetOfLikes(films.get(updatedFilm.getId()).getSetOfLikes());
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Данные о фильме id = {} обновлены", updatedFilm.getId());
        return updatedFilm;
    }

    @Override
    public List<Film> getListOfFilms() {
        log.info("Наплавоен список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int filmId) {
        log.info("Наплавоен фильм id={}", filmId);
        return films.get(filmId);
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
    public void deleteFilm(int filmId) {
        films.remove(filmId);
        log.info("Фильм с id = {} удален.", filmId);
    }

    public boolean isFilmInStorage(Film film) {
        return films.containsValue(film);
    }

    public boolean isFilmInStorage(int filmId) {
        return films.containsKey(filmId);
    }

    public boolean isFilmHasLikeFromUser(int filmId, int userId) {
        return films.get(filmId).getSetOfLikes().contains(userId);
    }

    private static boolean isDurationValid(Film film) throws JsonProcessingException {
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма {} отрицательная", mapper.writeValueAsString(film.getDuration()));
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
        return true;
    }

    public static boolean isFilmValid(Film film) throws JsonProcessingException {
        return isNameValid(film) && isDescriptionValid(film) && isReleaseDateValid(film) && isDurationValid(film);
    }

    public static boolean isReleaseDateValid(Film film) throws JsonProcessingException {
        if (film.getReleaseDate() == null) {
            log.warn("Отсутствует дата релиза");
            throw new ValidationException("Отсутствует дата релиза");
        } else if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            log.warn("Дата релиза {} раньше 28 декабря 1895 года", mapper.writeValueAsString(film.getReleaseDate()));
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года");
        }
        return true;
    }

    public static boolean isDescriptionValid(Film film) throws JsonProcessingException {
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма {} отрицательная", mapper.writeValueAsString(film.getDuration()));
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.warn("Отсутствует описание");
            throw new ValidationException("Отсутствует описание");
        } else if (film.getDescription().length() > 200) {
            log.warn("Длина описания:{}. Больше 200 символов.", film.getDescription().length());
            throw new ValidationException("Длина описания больше 200 символов");
        }
        return true;
    }

    public static boolean isNameValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Отсутствует название");
            throw new ValidationException("Отсутствует название");
        }
        return true;
    }
}