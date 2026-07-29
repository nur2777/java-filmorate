package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film mapFilmDTOToFilm(FilmDTO filmDTO) {

        Set<Long> genres = null;
        if (filmDTO.getGenres() != null) {
            genres = filmDTO.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        }
        Long ratingId = null;
        if (filmDTO.getMpa() != null) {
            ratingId = filmDTO.getMpa().getId();
        }

        Film film = Film.builder()
                .id(filmDTO.getId())
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .releaseDate(filmDTO.getReleaseDate())
                .duration(filmDTO.getDuration())
                .ratingId(ratingId)
                .genres(genres)
                .build();
        return film;
    }

    public static FilmDTO mapFilmtoFilmDTO(Film film) {

        List<Genre> genres = null;
        if (film.getGenres() != null) {
            genres = film.getGenres().stream()
                            .map(genreId -> Genre.builder()
                            .id(genreId)
                            .build())
                            .toList();
        }

        Mpa mpa = null;
        if (film.getRatingId() != null) {
            mpa = Mpa.builder()
                    .id(film.getRatingId())
                    .build();
        }

        FilmDTO filmDTO = FilmDTO.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .mpa(mpa)
                .genres(genres)
                .build();
        return filmDTO;
    }
}