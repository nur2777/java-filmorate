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

}
