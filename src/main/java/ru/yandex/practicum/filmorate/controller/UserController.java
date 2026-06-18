package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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

    /**
     * Вспомогательный метод для генерации идентификатора пользователя
     *
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

    /**
     * Эндпоинт на добавление пользователя
     *
     * @param newUser новый пользователь
     * @return объект созданного пользователя
     */
    @PostMapping
    public User add(@Valid @RequestBody User newUser) {
        return addNewUser(newUser);
    }

    /** Метод добавления нового пользователя
     * @param newUser данные нового пользователя
     * @return объект с добавленным пользователем
     */
    private User addNewUser(User newUser) {
        try {
            newUser.setId(getNextUserId());
            users.put(newUser.getId(), newUser);
            log.info("Пользователь {} успешно добавлен.", newUser.getName());
            return newUser;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Эндпоинт на обновление данных о пользователе
     *
     * @param user новые данные о пользователе
     * @return объект обновленного о пользователя
     */
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return updateUser(user);
    }

    /** Метод обновления данных о пользователе
     * @param user данные для обновления
     * @return обновлённый объект пользолвателя
     */
    private User updateUser(User user) {
        try {
            if (user.getId() == null) {
                throw new ValidationException("Не указан идентификатор пользователя");
            }
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
            throw e;
        }
    }

    /**
     * Эндпоинт получения списка всех пользователей
     *
     * @return список всех пользователей
     */
    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
