package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
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
    List<Genre> genre;
    Mpa mpa;

    public void generateAndSetId() {
        setId(++id);
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

    public void addGenre(Genre genreForAdd) {
        genre.add(genreForAdd);
    }
}