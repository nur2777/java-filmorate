package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class})
class FilmoRateGenreDbStrorageTests {

    @Autowired
    private GenreDbStorage genreStorage;


    @Test
    void testGetGenres() {
        Collection<Genre> genres = genreStorage.getGenres();

        assertThat(genres)
                .withFailMessage("Список жанров не должен быть null")
                .isNotNull();

        assertThat(genres)
                .withFailMessage("Количество жанров должно быть равно 6")
                .hasSize(6);
    }

    @Test
    void testGetGenre() {
        Long genreId = 1L; // Комедия
        Genre genre = genreStorage.getGenre(genreId);

        assertThat(genre.getId())
                .withFailMessage("ID жанра должен быть равен 1")
                .isEqualTo(1L);

        assertThat(genre.getName())
                .withFailMessage("Название жанра должно быть 'Комедия'")
                .isEqualTo("Комедия");
    }
}
