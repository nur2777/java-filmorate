package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Модель данных фильма
 */
@Data
@Builder
public class Film {
    /**
     * Идентификатор фильма
     */
    private Long id;
    /**
     * Название фильма
     */
    private String name;
    /**
     * Описание фильма
     */
    private String description;
    /**
     * Дата выхода фильма
     */
    private LocalDate releaseDate;
    /**
     * Продолжительность
     */
    private int duration;
    /**
     * Дата самого раннего фильма
     */
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    /**
     * Формат даты
     */
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Проверки для фильма
     *
     * @param film объект для проверки
     */
    public static void filmChecks(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Длина описания должна быть максимум 200 символов");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза должна быть не ранее " + MIN_RELEASE_DATE.format(dtf));
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
