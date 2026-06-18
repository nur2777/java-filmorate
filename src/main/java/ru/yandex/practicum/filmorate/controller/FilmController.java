package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс контроллер для фильмов
 */
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
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
    private Film addNewFilm(Film newFilm) {
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

    /**
     * Эндпоинт на добавление фильма
     *
     * @param newFilm новый фильм
     * @return объект добавленного фильма
     */
    @PostMapping
    public Film add(@Valid @RequestBody Film newFilm) {
        return addNewFilm(newFilm);
    }

    /**
     * Эндпоинт на обновление фильма
     *
     * @param film новые данные для обновления
     * @return объект обновленного фильма
     */
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return updateFilm(film);
    }

    /** Метод обновления данных о фильме
     * @param film обновленные данные о фильме
     * @return обновлённый объект фильма
     */
    private Film updateFilm(Film film) {
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

    /**
     * Эндпоинт получения списка всех фильмов
     *
     * @return список всех фильмов
     */
    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }
}
