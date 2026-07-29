package ru.yandex.practicum.filmorate.controller.film;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Интерфейс для контроллера по работе с фильмами
 */
public interface FilmController {
    /**
     * Эндпоинт на добавление фильма
     *
     * @param newFilm новый фильм
     * @return объект добавленного фильма
     */
    FilmDTO add(FilmDTO newFilm);

    /**
     * Эндпоинт на обновление фильма
     *
     * @param film новые данные для обновления
     * @return объект обновленного фильма
     */
    FilmDTO update(FilmDTO film);

    /**
     * Эндпоинт получения конкретного фильма
     *
     * @param id идентификатор фильма
     * @return объект фильма
     */
    FilmDTO getFilm(Long id);

    /**
     * Эндпоинт получения списка всех фильмов
     *
     * @return список всех фильмов
     */
    @GetMapping
    Collection<Film> getAllFilms();

    /**
     * Эндпоинт на установку лайка фильму.
     *
     * @param id идентификатор фильма
     * @param userId идентификатор фильма
     * @return объект обновленного фильма
     */
    Film addLike(Long id,Long userId);

    /**
     * Эндпоинт на удаление лайка
     *
     * @param id идентификатор фильма у которого надо удалить лайк
     * @param userId идентификатор пользователя лайк которого надо удалить
     * @return объект обновленного фильма
     */
    Film dislike(Long id, Long userId);

    /**
     * Эндпоинт возвращает список из популярных фильмов по количеству лайков.
     *
     * @param count количество популярных фильмов которые нужно вернуть
     * @return список популярных фильмов
     */
    Collection<Film> getTopPopularFilms(int count);
}
