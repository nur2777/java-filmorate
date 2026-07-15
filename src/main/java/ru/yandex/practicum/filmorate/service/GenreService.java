package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(@Qualifier("genreDbStorage") GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre getGenre(Long genreId) {
        return genreDbStorage.getGenre(genreId);
    }

    public Collection<Genre> getGenres() {
        return genreDbStorage.getGenres();
    }

}
