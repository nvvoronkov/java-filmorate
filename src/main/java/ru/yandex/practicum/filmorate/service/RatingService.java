package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RatingService {
    private final RatingStorage ratingStorage;

    @Autowired
    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public Optional<MPA> getRatingById(Integer id) {
        return ratingStorage.getRatingById(id);
    }

    public List<MPA> getRatingAll() {
        return ratingStorage.getRatingAll();
    }
}
