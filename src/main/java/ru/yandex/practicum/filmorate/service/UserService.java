package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;

/**
 * Класс отвечает за такие операции с пользователями, как добавление в друзья,
 * удаление из друзей, вывод списка общих друзей.
 */
@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /** Метод получения  пользователя
     * @param userId идентификатор пользователя
     * @return объект пользователя
     */
    public User getUser(Long userId) {
        return userStorage.getUser(userId);
    }

    /** Метод добавления нового пользователя
     * @param newUser данные нового пользователя
     * @return объект нового пользователя
     */
    public User addNewUser(User newUser) {
        return userStorage.addNewUser(newUser);
    }

    /** Метод обновления данных о пользователе
     * @param user данные для обновления
     * @return объект обновленного пользователя
     */
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    /** Метод получения списка всех пользователей
     * @return список всех пользователей
     */
    public Collection<User> getAllUsers() {
        return userStorage.getUsers();
    }

    /** Метода добавления в друзья
     * @param userId пользователь к которому добавляются в друзья
     * @param newFriendId идентификатор нового друга
     */
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

    /** Метод исключающий пользователя из списка друзей
     * @param userId пользователь
     * @param friendId друг которого надо исключить
     */
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

    /** Метод получения списка друзей у пользователя
     * @param userId пользователь у которого надо получить список друзей
     * @return список общих друзей между двумя пользователями
     */
    public Collection<User> getFriends(Long userId) {
        log.trace("Проверяем существование и получаем объект пользователя");
        User user = userStorage.getUser(userId);
        return user.getFriends().stream()
                .map(id -> userStorage.getUser(id))
                .toList();
    }

    /** Метод получения списка общих друзей
     * @param userId пользователь
     * @param otherUserId другой пользователь у которого надо найти общих друзей
     * @return список общих друзей между двумя пользователями
     */
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
