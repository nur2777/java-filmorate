package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

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
    private String email;

    /**
     * Логин пользователя
     */
    private String login;

    /**
     * Имя для отображения
     */
    private String name;
    /**
     * Дата рождения
     */
    private LocalDate birthday;
    // Пробел
    public static final CharSequence SPACE = " ";

    public String getName() {
        if (name == null) {
            return login;
        } else {
            return name;
        }
    }

    /**
     * Проверки для пользователя
     *
     * @param user объект для проверки
     */
    public static User userChecks(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(SPACE)) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return user;
    }

}
