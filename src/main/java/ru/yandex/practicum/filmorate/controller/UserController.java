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
     * Эндпоинт получения конкретного пользователя
     *
     * @param id идентификатор пользователя
     * @return объект пользователя
     */
    @GetMapping("/{id}")
    public User getUser(@Valid @PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * Эндпоинт получения списка всех пользователей
     * @return список всех пользователей
     */
    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Эндпоинт добавления в друзья.
     *
     * @param id идентификатор пользователя к которому нужно добавить друга
     * @param friendId идентификатор друга которого нужно добавить
     * @return объект обновленного пользователя
     */
    @PutMapping("{id}/friends/{friendId}")
    public User addNewFriend(@Valid @PathVariable Long id, @PathVariable Long friendId) {
        return userService.addNewFriend(id,friendId);
    }

    /**
     * Эндпоинт удаление из друзей.
     *
     * @param id идентификатор пользователя у которого нужно удалить друга
     * @param friendId идентификатор друга которого нужно удалить
     * @return объект обновленного пользователя
     */
    @DeleteMapping("{id}/friends/{friendId}")
    public User unfriend(@Valid @PathVariable Long id, @PathVariable Long friendId) {
        return userService.unfriend(id,friendId);
    }

    /**
     * Эндпоинт получения список пользователей, являющихся друзьями
     *
     * @param id идентификатор пользователя
     * @return список друзей
     */
    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@Valid @PathVariable Long id) {
        return userService.getFriends(id);
    }

    /**
     * Эндпоинт получения списка друзей, общих с другим пользователем.
     *
     * @param id идентификатор пользователя
     * @param otherId идентификатор другого пользователя
     * @return список друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@Valid @PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id,otherId);
    }
}
