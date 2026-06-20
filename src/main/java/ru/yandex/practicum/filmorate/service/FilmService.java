package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Класс отвечает за операции с фильмами — добавление и удаление лайка, вывод 10 наиболее
 * популярных фильмов по количеству лайков.
 */
@Service
@Slf4j
public class FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /** Метод получения фильма
     * @param filmId идентификатор фильма
     * @return объект фильма
     */
    public Film getFilm(Long filmId) {
        return filmStorage.getFilm(filmId);
    }

    /**
     * Метод добавления фильма
     * @param newFilm новый фильм
     * @return объект добавленного фильма
     */
    public Film addNewFilm(Film newFilm) {
        return filmStorage.addNewFilm(newFilm);
    }

    /**
     * Метод обновления фильма
     * @param film новые данные для обновления
     * @return объект обновленного фильма
     */
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    /**
     * Метод получения списка всех фильмов
     * @return список всех фильмов
     */
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    /** Метод добавления лайка для фильма
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    public Film addLike(Long filmId, Long userId) {
        log.trace("Проверяем существование и получаем объект пользователя");
        User user = userStorage.getUser(userId);
        log.trace("Проверяем существование и получаем объект фильма");
        Film film = filmStorage.getFilm(filmId);

        if (film.getLikes().add(userId)) {
            log.trace("Лайк пользователем успешно поставлен");
        } else {
            log.warn("Пользователь с id {} уже уже ранее поставил лайк.", userId);
        }
        return film;
    }

    /** Метод исключения лайка у фильма
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    public Film dislike(Long filmId, Long userId) {
        log.trace("Проверяем существование и получаем объект пользователя");
        User user = userStorage.getUser(userId);
        log.trace("Проверяем существование и получаем объект фильма");
        Film film = filmStorage.getFilm(filmId);

        if (film.getLikes().remove(userId)) {
            log.trace("Лайк пользователя успешно исключен");
        } else {
            log.warn("Пользователь с id {} не ставил лайк этому фильму.", userId);
        }
        return film;
    }

    /** Метода возвращает список популярных фильмов по количеству лайков.
     * @param count количество фильмов для возврата (топ первых)
     * @return список фильмов
     */
    public Collection<Film> getTopPopularFilms(int count) {
        return filmStorage.getFilms().stream()
                .filter(film -> !film.getLikes().isEmpty())
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
