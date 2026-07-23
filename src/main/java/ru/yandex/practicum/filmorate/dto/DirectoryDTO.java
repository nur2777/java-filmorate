package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Стандартный DTO слой для справочников с полями id и name
 */
@Data
@Builder
public class DirectoryDTO {
    /**
     * Идентификатор значения
     */
    private Long id;
    /**
     * Наименование значения
     */
    private String name;
}
