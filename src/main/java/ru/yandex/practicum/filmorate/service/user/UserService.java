package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Интерфейс отвечает за такие операции с пользователями, как добавление в друзья,
 * удаление из друзей, вывод списка общих друзей.
 */
public interface UserService {

    /** Метод получения пользователя
     * @param userId идентификатор пользователя
     * @return объект пользователя
     */
    User getUser(Long userId);

    /** Метод добавления нового пользователя
     * @param newUser данные нового пользователя
     * @return объект нового пользователя
     */
    User addNewUser(User newUser);

    /** Метод обновления данных о пользователе
     * @param user данные для обновления
     * @return объект обновленного пользователя
     */
    User updateUser(User user);

    /** Метод получения списка всех пользователей
     * @return список всех пользователей
     */
    Collection<User> getAllUsers();

    /** Метода добавления в друзья
     * @param userId пользователь к которому добавляются в друзья
     * @param newFriendId идентификатор нового друга
     */
    User addNewFriend(Long userId, Long newFriendId);

    /** Метод исключающий пользователя из списка друзей
     * @param userId пользователь
     * @param friendId друг которого надо исключить
     */
    User unfriend(Long userId, Long friendId);

    /** Метод получения списка друзей у пользователя
     * @param userId пользователь у которого надо получить список друзей
     * @return список общих друзей между двумя пользователями
     */
    Collection<User> getFriends(Long userId);

    /** Метод получения списка общих друзей
     * @param userId пользователь
     * @param otherUserId другой пользователь у которого надо найти общих друзей
     * @return список общих друзей между двумя пользователями
     */
    Collection<User> getCommonFriends(Long userId, Long otherUserId);
}
