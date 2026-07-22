package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    /**
     * Список фильмов
     */
    private final Map<Long, Film> films = new HashMap<>();

    /**
     * Вспомогательный метод для генерации идентификатора фильма
     *
     * @return новый идентификатор
     */
    private long getNextFilmId() {
        long currentFilmId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentFilmId;
    }

    @Override
    public Film addNewFilm(Film newFilm) {
        try {
            newFilm.setId(getNextFilmId());
            newFilm.setLikes(new HashSet<>());
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм {} успешно добавлен.", newFilm.getName());
            return newFilm;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            if (film.getId() == null) {
                throw new ValidationException("Не указан идентификатор фильма");
            }
            if (films.containsKey(film.getId())) {
                Film oldFilm = films.get(film.getId());
                oldFilm.setName(film.getName());
                oldFilm.setDescription(film.getDescription());
                oldFilm.setReleaseDate(film.getReleaseDate());
                oldFilm.setDuration(film.getDuration());
                log.info("Фильм {} успешно обновлен.", film.getName());
                return oldFilm;
            }
            throw new NotFoundException("Не найден фильм с идентификатором " + film.getId());
        } catch (ValidationException | NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteFilm(Long id) {
        log.info("Выполняется удаление фильма!");
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(Long id) {
        if (id == null) {
            throw new ValidationException("Не указан идентификатор фильма");
        }
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException("Не найден фильм с идентификатором " + id);
    }

    @Override
    public boolean addLike(Film film, Long userId) {
        return film.getLikes().add(userId);
    }

    @Override
    public boolean dislike(Film film, Long userId) {
        return film.getLikes().remove(userId);
    }
}
