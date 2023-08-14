package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/{id}")
    public MPA getMpaById(@PathVariable Integer id) {
        if (ratingService.getRatingById(id).isPresent()) {
            return ratingService.getRatingById(id).get();
        }
        return null;
    }

    @GetMapping
    public List<MPA> getMpaAll() {
        return ratingService.getRatingAll();
    }
}
