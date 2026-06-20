package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

/**
 * Класс контроллер для фильмов
 */
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Эндпоинт на добавление фильма
     *
     * @param newFilm новый фильм
     * @return объект добавленного фильма
     */
    @PostMapping
    public Film add(@Valid @RequestBody Film newFilm) {
        return filmService.addNewFilm(newFilm);
    }

    /**
     * Эндпоинт на обновление фильма
     *
     * @param film новые данные для обновления
     * @return объект обновленного фильма
     */
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    /**
     * Эндпоинт получения конкретного фильма
     *
     * @param id идентификатор фильма
     * @return объект фильма
     */
    @GetMapping("/{id}")
    public Film getFilm(@Valid @PathVariable Long id) {
        return filmService.getFilm(id);
    }

    /**
     * Эндпоинт получения списка всех фильмов
     *
     * @return список всех фильмов
     */
    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getFilms();
    }

    /**
     * Эндпоинт на установку лайка фильму.
     *
     * @param id идентификатор фильма
     * @param userId идентификатор фильма
     * @return объект обновленного фильма
     */
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId ) {
        return filmService.addLike(id,userId);
    }

    /**
     * Эндпоинт на удаление лайка
     *
     * @param id идентификатор фильма у которого надо удалить лайк
     * @param userId идентификатор пользователя лайк которого надо удалить
     * @return объект обновленного фильма
     */
    @DeleteMapping("/{id}/like/{userId}")
    public Film dislike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId ) {
        return filmService.dislike(id,userId);
    }

    /**
     * Эндпоинт возвращает список из популярных фильмов по количеству лайков.
     *
     * @param count количество популярных фильмов которые нужно вернуть
     * @return список популярных фильмов
     */
    @GetMapping("/popular")
    public Collection<Film> getTopPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopPopularFilms(count);
    }
}
