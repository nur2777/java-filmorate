package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

/**
 * Класс контроллер для пользователей
 */
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Эндпоинт на добавление пользователя
     * @param newUser новый пользователь
     * @return объект созданного пользователя
     */
    @PostMapping
    public User add(@Valid @RequestBody User newUser) {
        return userService.addNewUser(newUser);
    }

    /**
     * Эндпоинт на обновление данных о пользователе
     *
     * @param user новые данные о пользователе
     * @return объект обновленного пользователя
     */
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * Эндпоинт получения списка всех пользователей
     * @return список всех пользователей
     */
    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
