package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:TestFilmData.sql")
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    void shouldGetAllFilms() {
        List<Film> filmsList = filmDbStorage.getAll();
        assertThatList(filmsList).hasSizeBetween(4, 4);
    }

    @Test
    void shouldAddLike() {
        filmDbStorage.addLike(3, 1);
        filmDbStorage.addLike(3, 2);
        List<Film> popularFilms = filmDbStorage.getPopularFilms(4);
        Film expected = filmDbStorage.getById(3);
        Film actual = popularFilms.get(0);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldDeleteLike() {
        filmDbStorage.addLike(3, 1);
        filmDbStorage.addLike(3, 2);
        filmDbStorage.deleteLike(3, 1);
        filmDbStorage.deleteLike(3, 2);
        List<Film> popularFilms = filmDbStorage.getPopularFilms(4);
        Film actual = popularFilms.get(2);
        assertThat(actual).isEqualTo(filmDbStorage.getById(3));
    }

    @Test
    void shouldGetPopularFilms() {
        filmDbStorage.addLike(1, 1);
        filmDbStorage.addLike(1, 2);
        filmDbStorage.addLike(2, 1);
        List<Film> popularFilms = filmDbStorage.getPopularFilms(3);
        System.out.println(popularFilms.toString());
        Film actual1 = popularFilms.get(0);
        Film actual2 = popularFilms.get(1);
        Film actual3 = popularFilms.get(2);
        assertThat(actual1).isEqualTo(filmDbStorage.getById(1));
        assertThat(actual2).isEqualTo(filmDbStorage.getById(2));
        assertThat(actual3).isEqualTo(filmDbStorage.getById(3));
        assertThatList(popularFilms).hasSizeBetween(3, 3);
    }
}