package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validator.MinDateRelease;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    int id;
    @NotBlank
    String name;
    @NotBlank
    @Size(min = 1, max = 200)
    String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @MinDateRelease
    @Past(message = "Неверная дате выхода фильма")
    LocalDate releaseDate;
    @NotNull
    @Positive
    int duration;
}