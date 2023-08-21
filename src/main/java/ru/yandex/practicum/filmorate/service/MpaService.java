package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage ratingStorage) {
        this.mpaDbStorage = ratingStorage;
    }

    public Optional<Mpa> getRatingById(Integer id) {
        return mpaDbStorage.getById(id);
    }

    public List<Mpa> getRatingAll() {
        return mpaDbStorage.getAll();
    }
}
