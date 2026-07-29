package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinDate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.Film.MIN_RELEASE_DATE;

@Data
@Builder
public class FilmDTO {

    private Long id;
    @NotNull(message = "Название фильма не может быть пустым")
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Длина описания должна быть максимум 200 символов")
    private String description;
    @MinDate(minDate = MIN_RELEASE_DATE,message = "Дата релиза должна быть не ранее " + MIN_RELEASE_DATE)
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private int duration;
    private Mpa mpa;
    private List<Genre> genres;
}
