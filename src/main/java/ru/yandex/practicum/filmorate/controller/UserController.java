package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс контроллер для пользователей
 */
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    /**
     * Список пользователей
     */
    private final Map<Long, User> users = new HashMap<>();
    // Пробел
    public static final CharSequence SPACE = " ";
    /** Проверки для пользователя
     * @param user объект для проверки
     */
    private User userChecks(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ");
        }
        if (user.getLogin() == null || user.getLogin().contains(SPACE)) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }

    /** Вспомогательный метод для генерации идентификатора пользователя
     * @return новый идентификатор пользователя
     */
    private long getNextUserId() {
        long currentUserId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentUserId;
    }

    /** Эндпоинт на добавление пользователя
     * @param newUser новый пользователь
     * @return объект созданного пользователя
     */
    @PostMapping
    public User add(@RequestBody User newUser) {
        try {
            User user = userChecks(newUser);
            user.setId(getNextUserId());
            users.put(newUser.getId(), user);
            log.info("Пользователь {} успешно добавлен.", user.getName());
            return user;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /** Эндпоинт на обновление данных о пользователе
     * @param user новые данные о пользователе
     * @return объект обновленного о пользователя
     */
    @PutMapping
    public User update(@RequestBody User user) {
        try {
            if (user.getId() == null) {
                throw new ValidationException("Не указан идентификатор пользователя");
            }
            user = userChecks(user);
            if (users.containsKey(user.getId())) {
                User oldUser = users.get(user.getId());
                oldUser.setEmail(user.getEmail());
                oldUser.setLogin(user.getLogin());
                oldUser.setName(user.getName());
                oldUser.setBirthday(user.getBirthday());
                log.info("Пользователь {} успешно обновлен.", oldUser.getName());
                return oldUser;
            }
            throw new NotFoundException("Не найден пользователь с идентификатором " + user.getId());
        } catch (ValidationException | NotFoundException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /** Эндпоинт получения списка всех пользователей
     * @return список всех пользователей
     */
    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
