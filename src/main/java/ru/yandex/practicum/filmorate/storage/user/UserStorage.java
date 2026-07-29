package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

/**
 * Интерфейс в котором будут определены методы добавления, удаления и модификации пользователей.
 */
public interface UserStorage {
    /** Метод добавления пользователя
     * @param newUser данные нового пользователя
     * @return объект созданного пользователя
     */
    User addNewUser(User newUser);

    /** Метод обновления пользователя
     * @param user данные обновляемого пользователя
     * @return объект обновленного фильма
     */
    User updateUser(User user);

    /** Метод удаления пользователя
     * @param id идентификатор пользователя
     */
    void deleteUser(Long id);

    /** Метод получения списка всех пользователей
     * @return список пользователей
     */
    Collection<User> getUsers();

    /** Метод получения одного пользователя по идентификатору
     * @return объект пользователя
     */
    User getUser(Long id);

    /** Метод добавления друга
     * @param user данные пользователя к которому в друзья добавляется друг
     * @param newFriendId идентификатор добавляемого друга
     * @return true - если успешно добавлен, false - если друг уже существует в списке друзей
     */
    boolean addNewFriend(User user,Long newFriendId);

    /** Метод удаления друга
     * @param user данные пользователя у которого исключается друг
     * @param removeFriendId идентификатор удаляемого друга
     * @return true - если успешно исключен, false - если друга не существует в списке друзей
     */
    boolean unfriend(User user,Long removeFriendId);
}
