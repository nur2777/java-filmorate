package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constants.Qualifiers;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final MpaDbStorage mpaDbStorage;
    private final UserStorage userStorage;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmServiceImpl(@Qualifier(Qualifiers.FILM) FilmStorage filmStorage,
                           @Qualifier(Qualifiers.USER) UserStorage userStorage,
                           @Qualifier(Qualifiers.MPA) MpaDbStorage mpaDbStorage,
                           @Qualifier(Qualifiers.GENRE) GenreDbStorage genreDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public FilmDTO getFilm(Long filmId) {
        FilmDTO filmDTO = FilmMapper.mapFilmtoFilmDTO(filmStorage.getFilm(filmId));
        if (filmDTO.getMpa() != null) {
            filmDTO.setMpa(Mpa.builder()
                    .id(filmDTO.getMpa().getId())
                    .name(mpaDbStorage.getMpaRating(filmDTO.getMpa().getId()).getName())
                    .build());
        }
        for (Genre genre : filmDTO.getGenres()) {
            genre.setName(genreDbStorage.getGenre(genre.getId()).getName());
        }
        return filmDTO;
    }

    @Override
    public FilmDTO addNewFilm(FilmDTO newFilm) {
        Film film = FilmMapper.mapFilmDTOToFilm(newFilm);
        if (film.getRatingId() != null) {
            Mpa mpa = mpaDbStorage.getMpaRating(film.getRatingId());
        }
        if (film.getGenres() != null) {
            List<Genre> genreList = film.getGenres().stream()
                    .map(genreDbStorage::getGenre)
                    .toList();
        }
        return FilmMapper.mapFilmtoFilmDTO(filmStorage.addNewFilm(film));
    }

    @Override
    public FilmDTO updateFilm(FilmDTO filmDto) {
        Film film = FilmMapper.mapFilmDTOToFilm(filmDto);
        if (film.getRatingId() != null) {
            Mpa mpa = mpaDbStorage.getMpaRating(film.getRatingId());
        }
        if (film.getGenres() != null) {
            List<Genre> genreList = film.getGenres().stream()
                    .map(genreDbStorage::getGenre)
                    .toList();
        }
        return FilmMapper.mapFilmtoFilmDTO(filmStorage.updateFilm(film));
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
        if (filmStorage.addLike(film,userId)) {
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
        if (filmStorage.dislike(film,userId)) {
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
