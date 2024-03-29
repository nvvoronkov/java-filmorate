package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatingController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable Integer id) {
        return mpaService.getRatingById(id);
    }

    @GetMapping
    public List<Mpa> getMpaAll() {
        return mpaService.getRatingAll();
    }
}
