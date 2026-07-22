package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class})
class FilmoRateMpaDbStrorageTests {

    @Autowired
    private MpaDbStorage mpaDbStorage;


    @Test
    void testGetMpaRatings() {
        Collection<Mpa> ratings = mpaDbStorage.getMpaRatings();

        assertThat(ratings)
                .withFailMessage("Список рейтингов не должен быть null")
                .isNotNull();

        assertThat(ratings)
                .withFailMessage("Количество рейтингов должно быть равно 5")
                .hasSize(5);
    }

    @Test
    void testGetMpaRating() {
        Long ratingId = 1L; // G
        Mpa rating = mpaDbStorage.getMpaRating(ratingId);

        assertThat(rating)
                .withFailMessage("Найденный рейтинг не должен быть null")
                .isNotNull();

        assertThat(rating.getId())
                .withFailMessage("ID рейтинга должен быть равен 1")
                .isEqualTo(1L);

        assertThat(rating.getName())
                .withFailMessage("Код рейтинга должен быть 'G'")
                .isEqualTo("G");
    }
}
