package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * Модель данных пользователя
 */
@Data
@Builder
public class User {
    /**
     * Идентификатор пользователя
     */
    private Long id;
    /**
     * Электронная почта
     */
    @NotNull(message = "Электронная почта не может быть пустой!")
    @Email(message = "Электронная почта должна содержать символ @")
    private String email;
    /**
     * Логин пользователя
     */
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;
    /**
     * Имя для отображения
     */
    private String name;
    /**
     * Дата рождения
     */
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    // Пробел
    public static final CharSequence SPACE = " ";
    /**
     * Список друзей пользователя
     */
    private Set<Long> friends;

    public String getName() {
        if (name == null) {
            return login;
        } else {
            return name;
        }
    }

}
