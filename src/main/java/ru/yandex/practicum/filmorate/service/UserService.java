package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;

/**
 * Класс отвечает за такие операции с пользователями, как добавление в друзья,
 * удаление из друзей, вывод списка общих друзей.
 */
@Service
@Slf4j
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /** Метода добавления в друзья
     * @param userId пользователь к которому добавляются в друзья
     * @param newFriendId идентификатор нового друга
     */
    public void addNewFriend(Long userId, Long newFriendId){
        log.trace("Проверяем и получаем объект пользователя");
        User user = userStorage.getUser(userId);
        log.trace("Проверяем и получаем объект друга");
        User friend = userStorage.getUser(newFriendId);
        Set<Long> userFriends = user.getFriends();
        if (!userFriends.contains(newFriendId)) { // проверяем существование друга в списке
            userFriends.add(newFriendId);
            log.trace("Новый друг к пользователю успешно добавлен");
        } else {
            log.warn("Друг с id {} уже является другом пользователю.", newFriendId);
        }
        // Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
        // То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
        Set<Long> friendFriends = friend.getFriends();
        if (!friendFriends.contains(userId)) {  // проверяем существование пользователя в списке друзей у друга
            friendFriends.add(userId);
            log.trace("Пользователь успешно добавлен в друзья к другу");
        } else {
            log.warn("Пользователь с id {} уже является другом друга.", userId);
        }
    };
}
