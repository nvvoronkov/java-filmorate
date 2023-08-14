package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
    static int identificator = 0;
    int id;
    @NotBlank(message = "Не указанно название фильма")
    String name;
    @NotBlank
    @Size(max = 200, message = "Длительность описания должна состалять от 0 до 200 символов")
    String description;
    @NotNull(message = "Не указана дата релиза")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Неверная дате выхода фильма")
    LocalDate releaseDate;
    @NotNull
    @Positive
    int duration;
    int rate;
    Set<Integer> setOfLikes;
    LinkedHashSet<Genre> genre;
    MPA mpa;

    public void generateAndSetId() {
        setId(++identificator);
    }

    public void generateSetOfLikes() {
        this.setOfLikes = new HashSet<>();
    }

    public void addLike(int userId) {
        setOfLikes.add(userId);
    }

    public void deleteLike(int userId) {
        setOfLikes.remove(userId);
    }
}