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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@RequiredArgsConstructor
@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage{

    protected final JdbcTemplate jdbc;

    @Override
    public Film addNewFilm(Film newFilm) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String insert_query = "INSERT INTO films(name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert_query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,newFilm.getName());
            ps.setString(2,newFilm.getDescription());
            ps.setDate(3, Date.valueOf(newFilm.getReleaseDate()));
            ps.setInt(4, newFilm.getDuration());
            ps.setLong(5, newFilm.getRatingId());
            return ps;
            }, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);
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
            String update_query = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, " +
                    "rating_id = ? WHERE id = ?";
            int rowsUpdated = jdbc.update(update_query, film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getRatingId(),
                    film.getId());
            if (rowsUpdated == 0) {
                throw new NotFoundException("Не удалось обновить данные. Не найден фильм с идентификатором " + film.getId());
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
        String delete_query = "DELETE FROM films WHERE id = ?";
        int rowsDeleted = jdbc.update(delete_query, id);
        if (rowsDeleted == 0) {
            throw new NotFoundException("Не удалось удалить данные. Не найден фильм с идентификатором " + id);
        } else {
            log.info("Фильм успешно удален.");
        }
    }

    @Override
    public Collection<Film> getFilms() {
        String query = "SELECT  id, name, description, release_date, duration, rating_id FROM films";
        return jdbc.query(query, new FilmRowMapper());
    }

    @Override
    public Film getFilm(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор фильма");
        }
        String query = "SELECT id, name, description, release_date, duration, rating_id FROM films WHERE id = ?";
        try {
            Film result = jdbc.queryForObject(query, new FilmRowMapper(), id);
            return result;
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Не найден фильм с идентификатором " + id);
        }
    }
}
