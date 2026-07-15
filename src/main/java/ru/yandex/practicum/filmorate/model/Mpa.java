package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

/**
 * Модель данных жанра
 */
@Data
@Builder
public class Mpa {
    /**
     * Идентификатор рейтинга
     */
    private Long id;
    /**
     * Название рейтинга
     */
    private String name;
}
