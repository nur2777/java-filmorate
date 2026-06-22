package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User getUser(Long userId) {
        return userStorage.getUser(userId);
    }

    @Override
    public User addNewUser(User newUser) {
        return userStorage.addNewUser(newUser);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User addNewFriend(Long userId, Long newFriendId) {
        log.trace("Проверяем существование и получаем объект пользователя");
        User user = userStorage.getUser(userId);
        log.trace("Проверяем существование и получаем объект друга");
        User friend = userStorage.getUser(newFriendId);
        if (user.getFriends().add(newFriendId)) {
            log.trace("Новый друг к пользователю успешно добавлен");
        } else {
            log.warn("Друг с id {} уже является другом пользователю.", newFriendId);
        }
        // Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
        // То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
        if (friend.getFriends().add(userId)) {
            log.trace("Пользователь успешно добавлен в друзья к другу");
        } else {
            log.warn("Пользователь с id {} уже является другом друга.", userId);
        }
        return user;
    }

    @Override
    public User unfriend(Long userId, Long friendId) {
        log.trace("Проверяем существование и получаем объект пользователя");
        User user = userStorage.getUser(userId);
        log.trace("Проверяем существование и получаем объект друга");
        User friend = userStorage.getUser(friendId);
        if (user.getFriends().remove(friendId)) {
            log.trace("Друг удален из списка друзей пользователя успешно");
        } else {
            log.warn("Друг с id {} не является другом пользователю.", friendId);
        }
        // Удаление пользователя из списка друзей друга
        if (friend.getFriends().remove(userId)) {
            log.trace("Пользователь успешно удален из списка друзей друга");
        } else {
            log.warn("Пользователь с id {} не является другом друга.", userId);
        }
        return user;
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        log.trace("Проверяем существование и получаем объект пользователя");
        User user = userStorage.getUser(userId);
        return user.getFriends().stream()
                .map(id -> userStorage.getUser(id))
                .toList();
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        log.trace("Проверяем существование и получаем объект пользователя");
        User user = userStorage.getUser(userId);
        log.trace("Проверяем существование и получаем объект друга");
        User otherUser = userStorage.getUser(otherUserId);
        return user.getFriends().stream()
                        .filter(otherUser.getFriends()::contains)
                        .map(id -> userStorage.getUser(id))
                        .toList();
    }

}
