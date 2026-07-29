package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Интерфейс отвечает за операции с фильмами — добавление и удаление лайка, вывод 10 наиболее
 * популярных фильмов по количеству лайков.
 */
public interface FilmService {

    /** Метод получения фильма
     * @param filmId идентификатор фильма
     * @return объект фильма
     */
    FilmDTO getFilm(Long filmId);

    /**
     * Метод добавления фильма
     * @param newFilm новый фильм
     * @return объект добавленного фильма
     */
    FilmDTO addNewFilm(FilmDTO newFilm);

    /**
     * Метод обновления фильма
     * @param film новые данные для обновления
     * @return объект обновленного фильма
     */
    FilmDTO updateFilm(FilmDTO film);

    /**
     * Метод получения списка всех фильмов
     * @return список всех фильмов
     */
    Collection<Film> getFilms();

    /** Метод добавления лайка для фильма
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    Film addLike(Long filmId, Long userId);

    /** Метод исключения лайка у фильма
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    Film dislike(Long filmId, Long userId);

    /** Метода возвращает список популярных фильмов по количеству лайков.
     * @param count количество фильмов для возврата (топ первых)
     * @return список фильмов
     */
    Collection<Film> getTopPopularFilms(int count);
}
