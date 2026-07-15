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
 * Модель данных жанра
 */
@Data
@Builder
public class Genre {
    /**
     * Идентификатор жанра
     */
    private Long id;
    /**
     * Название жанра
     */
    private String name;
}
