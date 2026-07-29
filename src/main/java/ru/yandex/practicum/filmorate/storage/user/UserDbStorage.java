package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constants.Qualifiers;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.mappers.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component(Qualifiers.USER)
@Slf4j
public class UserDbStorage implements UserStorage {

    protected final JdbcTemplate jdbc;

    @Override
    public User addNewUser(User newUser) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String insertQuery = "INSERT INTO users (name, email, birthday, login) VALUES (?, ?, ?, ?)";
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,newUser.getName());
            ps.setString(2,newUser.getEmail());
            ps.setDate(3, Date.valueOf(newUser.getBirthday()));
            ps.setString(4, newUser.getLogin());
            return ps;
        }, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id != null) {
            return getUser(Long.valueOf(id));
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            if (user.getId() == null) {
                throw new ValidationException("Не указан идентификатор пользователя");
            }
            String updateQuery = "UPDATE users SET name = ?, email = ?, birthday = ?, login = ? WHERE id = ?";
            int rowsUpdated = jdbc.update(updateQuery, user.getName(),
                    user.getEmail(),
                    user.getBirthday(),
                    user.getLogin(),
                    user.getId());
            if (rowsUpdated == 0) {
                throw new NotFoundException("Не удалось обновить данные. Не найден пользователь с идентификатором " + user.getId());
            } else {
                return getUser(user.getId());
            }
        } catch (ValidationException | NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор пользователя");
        }
        String deleteQuery = "DELETE FROM users WHERE id = ?";
        int rowsDeleted = jdbc.update(deleteQuery, id);
        if (rowsDeleted == 0) {
            throw new NotFoundException("Не удалось удалить данные. Не найден пользователь с идентификатором " + id);
        } else {
            log.info("Фильм успешно удален.");
        }
    }

    @Override
    public Collection<User> getUsers() {
        String query = "SELECT id, email, login, name, birthday FROM users";
        List<User> users = jdbc.query(query, new UserRowMapper()).stream()
                .map(user -> {
                                    user.setFriends(getUserFriends(user.getId()));
                                    return user;
                                    })
                .toList();

        return users;
    }

    @Override
    public User getUser(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор пользователя");
        }
        String query = "SELECT id, email, login, name, birthday FROM users WHERE id = ?";
        try {
            User result = jdbc.queryForObject(query, new UserRowMapper(), id);
            if (result != null) {
                result.setFriends(getUserFriends(id));
            }
            return result;
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Не найден пользователь с идентификатором " + id);
        }
    }

    @Override
    public boolean addNewFriend(User user, Long newFriendId) {
        if (newFriendId == null) {
            throw new ValidationException("Не указан идентификатор нового друга");
        }
        if (user.getFriends().contains(newFriendId)) {
            log.info("Друг c id={} уже добавлен в друзья.",newFriendId);
            return false;
        } else {
            String insert = """
                    INSERT INTO friends (user_id, friend_id)
                    VALUES (?, ?)
                    """;
            int rowsInserted = jdbc.update(insert, user.getId(), newFriendId);
            if (rowsInserted == 0) {
                throw new InternalServerException("Не удалось вставить данные в таблицу friends");
            } else {
                log.info("Друг c id={} успешно добавлен в друзья.", newFriendId);
                return true;
            }
        }
    }

    @Override
    public boolean unfriend(User user, Long removeFriendId) {
        if (removeFriendId == null) {
            throw new ValidationException("Не указан идентификатор удаляемого друга");
        }
        if (!user.getFriends().contains(removeFriendId)) {
            log.info("Друг c id={} не существует в друзьях у пользователя.",removeFriendId);
            return false;
        } else {
            String deleteQuery = """
                    DELETE from friends
                    WHERE user_id = ? and friend_id = ?
                    """;
            int rowsDeleted = jdbc.update(deleteQuery, user.getId(), removeFriendId);
            if (rowsDeleted == 0) {
                throw new NotFoundException("Не удалось удалить данные. Не найден пользователь с идентификатором " + user.getId() + " и его друг с friend_id " + removeFriendId);
            } else {
                log.info("Друг успешно удален.");
                return true;
            }
        }
    }

    /** Метод возвращает список идентификаторов друзей у пользователя
     * @param userId идентификатор пользователя
     * @return набор идентификаторов друзей у пользователя
     */
    private Set<Long> getUserFriends(Long userId) {
        String query = "SELECT friend_id FROM friends WHERE user_id = ?";
        List<Long> friendsList = jdbc.queryForList(query, Long.class, userId);
        return new HashSet<>(friendsList);
    }
}
