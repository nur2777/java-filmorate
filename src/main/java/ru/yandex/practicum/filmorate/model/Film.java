package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Модель данных фильма
 */
@Data
@Builder
public class Film {
    /**
     * Дата самого раннего фильма
     */
    private static final String MIN_RELEASE_DATE = "28.12.1895";
    /**
     * Формат даты
     */
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    /**
     * Идентификатор фильма
     */
    private Long id;
    /**
     * Название фильма
     */
    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    /**
     * Описание фильма
     */
    @Size(max = 200, message = "Длина описания должна быть максимум 200 символов")
    private String description;
    /**
     * Дата выхода фильма
     */
    @MinDate(minDate = MIN_RELEASE_DATE,message = "Дата релиза должна быть не ранее " + MIN_RELEASE_DATE)
    private LocalDate releaseDate;
    /**
     * Продолжительность
     */
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private int duration;
    /**
     * Список идентификаторов пользователей поставивших лайк фильму
     */
    private Set<Long> likes;
    /**
     * Список идентификаторов жанров у фильма
     */
    private Set<Long> genres;
    /**
     * Возрастное ограничение для фильма, идентификатор рейтинга
     */
    private Long ratingId;
}
