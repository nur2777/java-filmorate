package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final GenreDbStorage genreDbStorage;

    protected final JdbcTemplate jdbc;

    @Override
    public Film addNewFilm(Film newFilm) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String insertQuery = "INSERT INTO films(name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,newFilm.getName());
            ps.setString(2,newFilm.getDescription());
            ps.setDate(3, Date.valueOf(newFilm.getReleaseDate()));
            ps.setInt(4, newFilm.getDuration());
            // Используем setObject, который допускает null
            if (newFilm.getRatingId() != null) {
                ps.setLong(5, newFilm.getRatingId());
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            return ps;
            }, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);

        if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty()) {
            for (Long genreId: newFilm.getGenres().stream().toList()) {
                genreDbStorage.addGenreToFilm(Long.valueOf(id),genreId);
            }
        } else {
            log.warn("Фильм id = {} не имеет жанров", id);
        }

        if (id != null) {
            return getFilm(Long.valueOf(id));
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            if (film.getId() == null) {
                throw new ValidationException("Не указан идентификатор фильма");
            }
            String updateQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, " +
                    "rating_id = ? WHERE id = ?";
            int rowsUpdated = jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(updateQuery);
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getDuration());
                if (film.getRatingId() != null) {
                    ps.setLong(5, film.getRatingId());
                } else {
                    ps.setNull(5, Types.BIGINT);
                }
                ps.setLong(6, film.getId());
                return ps;
            });
            if (rowsUpdated == 0) {
                throw new NotFoundException("Не удалось обновить данные. Не найден фильм с идентификатором "
                        + film.getId());
            } else {
                return getFilm(film.getId());
            }
        } catch (ValidationException | NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteFilm(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор фильма");
        }
        String deleteQuery = "DELETE FROM films WHERE id = ?";
        int rowsDeleted = jdbc.update(deleteQuery, id);
        if (rowsDeleted == 0) {
            throw new NotFoundException("Не удалось удалить данные. Не найден фильм с идентификатором " + id);
        } else {
            log.info("Фильм успешно удален.");
        }
    }

    @Override
    public Collection<Film> getFilms() {
        String query = "SELECT  id, name, description, release_date, duration, rating_id FROM films";
        List<Film> films = jdbc.query(query, new FilmRowMapper()).stream()
                .map(film -> {
                    film.setLikes(getFilmLikes(film.getId()));
                    return film;
                })
                .map(film -> {
                    film.setGenres(getFilmGenres(film.getId()));
                    return film;
                })
                .toList();
         return films;
    }

    @Override
    public Film getFilm(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор фильма");
        }
        String query = "SELECT id, name, description, release_date, duration, rating_id FROM films WHERE id = ?";
        try {
            Film result = jdbc.queryForObject(query, new FilmRowMapper(), id);
            if (result != null) {
                result.setLikes(getFilmLikes(id));
                result.setGenres(getFilmGenres(id));
            }
            return result;
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Не найден фильм с идентификатором " + id);
        }
    }

    @Override
    public boolean addLike(Film film, Long userId) {
        if (userId == null) {
            throw new ValidationException("Не указан идентификатор пользователя");
        }
        if (film.getLikes().contains(userId)) {
            log.info("Пользователь c id={} уже поставил лайк этому фильму.",userId);
            return false;
        } else {
            String insert = """
                    INSERT INTO likes (film_id, user_id)
                    VALUES (?, ?)
                    """;
            int rowsInserted = jdbc.update(insert, film.getId(), userId);
            if (rowsInserted == 0) {
                throw new InternalServerException("Не удалось вставить данные в таблицу likes");
            } else {
                log.info("Пользователь c id={} успешно поставил лайк фильму", userId);
                return true;
            }
        }
    }

    @Override
    public boolean dislike(Film film, Long userId) {
        if (userId == null) {
            throw new ValidationException("Не указан идентификатор пользователя лайк которого удаляется");
        }
        if (!film.getLikes().contains(userId)) {
            log.info("Пользователь c id={} не ставил лайк этому фильму.",userId);
            return false;
        } else {
            String deleteQuery = """
                    DELETE from likes
                    WHERE film_id = ? and user_id = ?
                    """;
            int rowsDeleted = jdbc.update(deleteQuery, film.getId(), userId);
            if (rowsDeleted == 0) {
                throw new NotFoundException("Не удалось удалить данные. Не найден фильм с идентификатором " +
                        film.getId() + " и лайк от пользователя с user_id " + userId);
            } else {
                log.info("Лайк успешно удален.");
                return true;
            }
        }
    }

    /** Метод возвращает список идентификаторов пользователей поставивших лайк фильму
     * @param filmId идентификатор фильма
     * @return набор идентификаторов пользователей
     */
    private Set<Long> getFilmLikes(Long filmId) {
        String query = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> friendsList = jdbc.queryForList(query, Long.class, filmId);
        return new HashSet<>(friendsList);
    }

    /** Метод возвращает список идентификаторов жанров у фильма
     * @param filmId идентификатор фильма
     * @return набор идентификаторов жанров
     */
    private Set<Long> getFilmGenres(Long filmId) {
        String query = "SELECT genre_id FROM film_genres WHERE film_id = ?";
        List<Long> genreList = jdbc.queryForList(query, Long.class, filmId);
        return new HashSet<>(genreList);
    }
}
