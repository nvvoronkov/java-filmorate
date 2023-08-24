package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public Mpa getRatingById(Integer id) {
        if (mpaDbStorage.checkIsObjectInStorage(id)) {
            return mpaDbStorage.getById(id);
        } else {
            log.info("Mpa id={} не найден", id);
            throw new NotFoundException(String.format("Mpa id=%s не найден", id));
        }
    }

    public List<Mpa> getRatingAll() {
        return mpaDbStorage.getAll();
    }
}
