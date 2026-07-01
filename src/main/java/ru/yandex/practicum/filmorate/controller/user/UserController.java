package ru.yandex.practicum.filmorate.controller.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Интерфейс для контроллера по работе с пользователями
 */
public interface UserController {
    /**
     * Эндпоинт на добавление пользователя
     * @param newUser новый пользователь
     * @return объект созданного пользователя
     */
    User add(User newUser);

    /**
     * Эндпоинт на обновление данных о пользователе
     *
     * @param user новые данные о пользователе
     * @return объект обновленного пользователя
     */
    User update(User user);

    /**
     * Эндпоинт получения конкретного пользователя
     *
     * @param id идентификатор пользователя
     * @return объект пользователя
     */
    User getUser(Long id);

    /**
     * Эндпоинт получения списка всех пользователей
     * @return список всех пользователей
     */
    Collection<User> getAllUsers();

    /**
     * Эндпоинт добавления в друзья.
     *
     * @param id идентификатор пользователя к которому нужно добавить друга
     * @param friendId идентификатор друга которого нужно добавить
     * @return объект обновленного пользователя
     */
    User addNewFriend(Long id, Long friendId);

    /**
     * Эндпоинт удаление из друзей.
     *
     * @param id идентификатор пользователя у которого нужно удалить друга
     * @param friendId идентификатор друга которого нужно удалить
     * @return объект обновленного пользователя
     */
    User unfriend(Long id, Long friendId);

    /**
     * Эндпоинт получения список пользователей, являющихся друзьями
     *
     * @param id идентификатор пользователя
     * @return список друзей
     */
    Collection<User> getFriends(Long id);

    /**
     * Эндпоинт получения списка друзей, общих с другим пользователем.
     *
     * @param id идентификатор пользователя
     * @param otherId идентификатор другого пользователя
     * @return список друзей
     */
    Collection<User> getCommonFriends(Long id, Long otherId);
}
