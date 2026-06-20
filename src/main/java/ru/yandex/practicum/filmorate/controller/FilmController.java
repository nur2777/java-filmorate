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
     * Эндпоинт получения списка всех фильмов
     *
     * @return список всех фильмов
     */
    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getFilms();
    }
}
