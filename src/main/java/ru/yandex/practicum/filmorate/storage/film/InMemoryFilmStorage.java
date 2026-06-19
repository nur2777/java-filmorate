package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
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

    /** Метод с логикой добавления нового фильма
     * @param newFilm данные нового фильма
     * @return объект фильма
     */
    @Override
    public Film addNewFilm(Film newFilm) {
        try {
            newFilm.setId(getNextFilmId());
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм {} успешно добавлен.", newFilm.getName());
            return newFilm;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /** Метод обновления данных о фильме
     * @param film обновленные данные о фильме
     * @return обновлённый объект фильма
     */
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

    /** Метод удаляет фильм по идентификатору
     * @param id идентификатор фильма
     */
    @Override
    public void deleteFilm(Long id) {
        log.info("Выполняется удаление фильма!");
    }

    /** Метод возвращает список всех фильмов
     * @return список фильмов
     */
    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }
}
