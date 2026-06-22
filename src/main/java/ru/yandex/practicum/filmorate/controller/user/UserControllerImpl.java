package ru.yandex.practicum.filmorate.controller.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;

import java.util.Collection;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserControllerImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    @Override
    public User add(@Valid @RequestBody User newUser) {
        return userService.addNewUser(newUser);
    }

    @PutMapping
    @Override
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    @Override
    public User getUser(@Valid @PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    @Override
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("{id}/friends/{friendId}")
    @Override
    public User addNewFriend(@Valid @PathVariable Long id, @PathVariable Long friendId) {
        return userService.addNewFriend(id,friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    @Override
    public User unfriend(@Valid @PathVariable Long id, @PathVariable Long friendId) {
        return userService.unfriend(id,friendId);
    }

    @GetMapping("/{id}/friends")
    @Override
    public Collection<User> getFriends(@Valid @PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @Override
    public Collection<User> getCommonFriends(@Valid @PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id,otherId);
    }
}
