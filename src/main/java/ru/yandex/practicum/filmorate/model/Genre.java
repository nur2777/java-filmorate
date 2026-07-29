package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

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
