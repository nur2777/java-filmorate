package ru.yandex.practicum.filmorate.storage.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    /**
     * Список пользователей
     */
    private final Map<Long, User> users = new HashMap<>();

    /**
     * Вспомогательный метод для генерации идентификатора пользователя
     *
     * @return новый идентификатор пользователя
     */
    private long getNextUserId() {
        long currentUserId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentUserId;
    }

    /** Метод добавления нового пользователя
     * @param newUser данные нового пользователя
     * @return объект с добавленным пользователем
     */
    public User addNewUser(User newUser) {
        try {
            newUser.setId(getNextUserId());
            users.put(newUser.getId(), newUser);
            log.info("Пользователь {} успешно добавлен.", newUser.getName());
            return newUser;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /** Метод обновления данных о пользователе
     * @param user данные для обновления
     * @return обновлённый объект пользолвателя
     */
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

    /** Метод удаляет пользователя по идентификатору
     * @param id идентификатор пользователя
     */
    @Override
    public void deleteUser(Long id) {
        log.info("Выполняется удаление пользователя!");
    }

    /** Метод возвращает список всех пользователей
     * @return список пользователей
     */
    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    /** Метод проверки существования и получения одного пользователя по идентификатору
     * @return объект пользователя
     */
    @Override
    public User getUser(Long id){
        if (id == null) {
            throw new ValidationException("Не указан идентификатор пользователя");
        }
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Не найден пользователь с идентификатором " + id);
    };

}
