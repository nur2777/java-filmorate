package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constants.Qualifiers;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.mappers.MpaRowMapper;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component(Qualifiers.MPA)
@Slf4j
public class MpaDbStorage {

    protected final JdbcTemplate jdbc;

    public Collection<Mpa> getMpaRatings() {
        String query = "SELECT rating_id, rating_code FROM mpa_ratings";
        List<Mpa> ratings = jdbc.query(query, new MpaRowMapper());
        return ratings;
    }

    public Mpa getMpaRating(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор рейтинга");
        }
        String query = "SELECT rating_id, rating_code FROM mpa_ratings WHERE rating_id = ?";
        try {
            Mpa result = jdbc.queryForObject(query, new MpaRowMapper(), id);
            return result;
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Не найден рейтинг с идентификатором " + id);
        }
    }
}
