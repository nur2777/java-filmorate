package ru.yandex.practicum.filmorate.service.film;

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

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film getFilm(Long filmId) {
        return filmStorage.getFilm(filmId);
    }

    @Override
    public Film addNewFilm(Film newFilm) {
        return filmStorage.addNewFilm(newFilm);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
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

    @Override
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

    @Override
    public Collection<Film> getTopPopularFilms(int count) {
        return filmStorage.getFilms().stream()
                .filter(film -> !film.getLikes().isEmpty())
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
