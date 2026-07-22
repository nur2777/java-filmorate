package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.mappers.GenreRowMapper;

import java.util.*;

@RequiredArgsConstructor
@Component("genreDbStorage")
@Slf4j
public class GenreDbStorage {

    protected final JdbcTemplate jdbc;

    public Collection<Genre> getGenres() {
        String query = "SELECT genre_id, genre_name FROM genres";
        List<Genre> genres = jdbc.query(query, new GenreRowMapper());
        return genres;
    }

    public Genre getGenre(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор жанра");
        }
        String query = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
        try {
            Genre result = jdbc.queryForObject(query, new GenreRowMapper(), id);
            return result;
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Не найден жанр с идентификатором " + id);
        }
    }

    public void addGenreToFilm(Long filmId, Long genreId) {
        String insert = """
                        INSERT INTO film_genres (film_id, genre_id)
                        VALUES (?, ?)
                        """;
        jdbc.update(insert, filmId, genreId);
    }

}
