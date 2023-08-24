package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void shouldGetAllMpaRatings() {
        List<Mpa> listOfMpa = mpaDbStorage.getAll();
        assertThatList(listOfMpa).hasSizeBetween(6, 6);
        assertThat(listOfMpa.get(0)).isEqualTo(Mpa.builder().id(1).name("G").build());
    }

    @Test
    void shouldGetMpaRatingById() {
        Mpa actual = mpaDbStorage.getById(2);
        assertThat(actual).isEqualTo(Mpa.builder().id(2).name("PG")
                .build());
    }
}
