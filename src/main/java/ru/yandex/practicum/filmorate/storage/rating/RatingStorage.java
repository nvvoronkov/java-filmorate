package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface RatingStorage {
    Optional<MPA> getRatingById(Integer mpaId);

    List<MPA> getRatingAll();
}
