package ru.yandex.practicum.filmorate.storage.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    /**
     * Список пользователей
     */
    private final Map<Long, User> users = new HashMap<>();

    private long getNextUserId() {
        long currentUserId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentUserId;
    }

    public User addNewUser(User newUser) {
        try {
            newUser.setId(getNextUserId());
            newUser.setFriends(new HashSet<>());
            users.put(newUser.getId(), newUser);
            log.info("Пользователь {} успешно добавлен.", newUser.getName());
            return newUser;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public User updateUser(User user) {
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

    @Override
    public void deleteUser(Long id) {
        log.info("Выполняется удаление пользователя!");
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUser(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор пользователя");
        }
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Не найден пользователь с идентификатором " + id);
    }

    @Override
    public boolean addNewFriend(User user, Long newFriendId) {
        return user.getFriends().add(newFriendId);
    }

    @Override
    public boolean unfriend(User user, Long removeFriendId) {
        return user.getFriends().remove(removeFriendId);
    }

}
