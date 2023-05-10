package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import ch.qos.logback.core.util.Duration;
import lombok.Data;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
}
