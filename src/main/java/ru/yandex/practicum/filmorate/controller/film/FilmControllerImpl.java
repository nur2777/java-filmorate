package ru.yandex.practicum.filmorate.controller.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;

import java.util.Collection;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmControllerImpl implements FilmController {

    /**
     * Значение по умолчанию при пустом параметре count
     */
    private  static final String DEFAULT_TOP_COUNT = "10";
    private final FilmServiceImpl filmService;

    @Autowired
    public FilmControllerImpl(FilmServiceImpl filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    @Override
    public FilmDTO add(@Valid @RequestBody FilmDTO newFilm) {
        return filmService.addNewFilm(newFilm);
    }

    @PutMapping
    @Override
    public FilmDTO update(@Valid @RequestBody FilmDTO film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    @Override
    public FilmDTO getFilm(@Valid @PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @GetMapping
    @Override
    public Collection<Film> getAllFilms() {
        return filmService.getFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    @Override
    public Film addLike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        return filmService.addLike(id,userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Override
    public Film dislike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        return filmService.dislike(id,userId);
    }

    @GetMapping("/popular")
    @Override
    public Collection<Film> getTopPopularFilms(@RequestParam(defaultValue = DEFAULT_TOP_COUNT) int count) {
        return filmService.getTopPopularFilms(count);
    }
}
