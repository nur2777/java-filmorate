package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class,FilmDbStorage.class})
class FilmoRateFilmDbStrorageTests {

    @Autowired
    private FilmDbStorage filmStorage;
    @Autowired
    private UserDbStorage userStorage;

    private Film testFilm;
    private User testUser;

    @BeforeEach
    void beforeEach() {
        // Тестовый пользователь
        testUser = User.builder()
                .email("nradmir@gmail.com")
                .login("nradmir")
                .name("Radmir")
                .birthday(LocalDate.of(1990, 7, 12))
                .build();
        userStorage.addNewUser(testUser);

        // Создаем тестовый фильм
        testFilm = Film.builder()
                .name("Интерстеллар")
                .description("Описание фильма Интерстеллар")
                .releaseDate(LocalDate.of(2014, 11, 5))
                .duration(2)
                .ratingId(1L) // Рейтинг G
                .genres(Set.of(1L,2L))
                .build();
    }

    @Test
    public void testAddNewFilm_withCorrectData() {
        Film savedFilm = filmStorage.addNewFilm(testFilm);

        assertThat(savedFilm)
                .withFailMessage("Сохраненный фильм не должен быть null")
                .isNotNull();

        assertThat(savedFilm.getId())
                .withFailMessage("ID сохраненного фильма не должен быть null")
                .isNotNull();

        assertThat(savedFilm.getName())
                .withFailMessage("Имя фильма не совпадает с ожидаемым")
                .isEqualTo(testFilm.getName());

        assertThat(savedFilm.getDescription())
                .withFailMessage("Описание фильма не совпадает с ожидаемым")
                .isEqualTo(testFilm.getDescription());

        assertThat(savedFilm.getReleaseDate())
                .withFailMessage("Дата релиза фильма не совпадает с ожидаемой")
                .isEqualTo(testFilm.getReleaseDate());

        assertThat(savedFilm.getDuration())
                .withFailMessage("Продолжительность фильма не совпадает с ожидаемой")
                .isEqualTo(testFilm.getDuration());

        assertThat(savedFilm.getRatingId())
                .withFailMessage("ID рейтинга фильма не совпадает с ожидаемым")
                .isEqualTo(testFilm.getRatingId());

        assertThat(savedFilm.getGenres())
                .withFailMessage("Количество жанров должно быть равно 2")
                .hasSize(2);

        assertThat(savedFilm.getGenres())
                .withFailMessage("Жанры фильма должны содержать ID 1 и 2")
                .contains(1L, 2L);

        assertThat(savedFilm.getLikes())
                .withFailMessage("Список лайков должен быть пустым")
                .isEmpty();
    }

    @Test
    void testAddNewFilm_WithoutRatingId() {
        testFilm.setRatingId(null);
        Film savedFilm = filmStorage.addNewFilm(testFilm);

        assertThat(savedFilm)
                .withFailMessage("Сохраненный фильм не должен быть null")
                .isNotNull();

        assertThat(savedFilm.getId())
                .withFailMessage("ID сохраненного фильма не должен быть null")
                .isNotNull();

        assertThat(savedFilm.getRatingId())
                .withFailMessage("ID рейтинга должен быть null, так как не был указан ")
                .isNull();
    }

    @Test
    void testUpdateFilm_withCorrectData() {
        Film savedFilm = filmStorage.addNewFilm(testFilm);
        savedFilm.setName("Белое солнце пустыни");
        savedFilm.setDescription("Отличный советский фильм");
        savedFilm.setDuration(177);

        Film updatedFilm = filmStorage.updateFilm(savedFilm);

        assertThat(updatedFilm)
                .withFailMessage("Обновленный фильм не должен быть null")
                .isNotNull();

        assertThat(updatedFilm.getId())
                .withFailMessage("ID обновленного фильма должен совпадать с исходным")
                .isEqualTo(savedFilm.getId());

        assertThat(updatedFilm.getName())
                .withFailMessage("Имя фильма должно быть обновлено")
                .isEqualTo("Белое солнце пустыни");

        assertThat(updatedFilm.getDescription())
                .withFailMessage("Описание фильма должно быть обновлено")
                .isEqualTo("Отличный советский фильм");

        assertThat(updatedFilm.getDuration())
                .withFailMessage("Продолжительность фильма должна быть обновлена")
                .isEqualTo(177);

        assertThat(updatedFilm.getReleaseDate())
                .withFailMessage("Дата релиза не должна измениться")
                .isEqualTo(savedFilm.getReleaseDate());

        assertThat(updatedFilm.getRatingId())
                .withFailMessage("ID рейтинга не должен измениться")
                .isEqualTo(savedFilm.getRatingId());
    }

    @Test
    void testUpdateFilm_WhenIdIsNull() {
        Film filmWithoutId = Film.builder()
                .name("Фильм без идентификатора")
                .description("Описание")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();

        assertThatThrownBy(() -> filmStorage.updateFilm(filmWithoutId))
                .withFailMessage("Должно быть выброшено исключение ValidationException при обновлении фильма без ID")
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Не указан идентификатор фильма");
    }

    @Test
    void testGetFilm_withCorrectData() {
        Film savedFilm = filmStorage.addNewFilm(testFilm);
        Film foundFilm = filmStorage.getFilm(savedFilm.getId());

        assertThat(foundFilm)
                .withFailMessage("Найденный фильм не должен быть null")
                .isNotNull();

        assertThat(foundFilm.getId())
                .withFailMessage("ID найденного фильма должен совпадать с ожидаемым")
                .isEqualTo(savedFilm.getId());

        assertThat(foundFilm.getName())
                .withFailMessage("Имя фильма должно совпадать с ожидаемым")
                .isEqualTo(testFilm.getName());

        assertThat(foundFilm.getDescription())
                .withFailMessage("Описание фильма должно совпадать с ожидаемым")
                .isEqualTo(testFilm.getDescription());

        assertThat(foundFilm.getReleaseDate())
                .withFailMessage("Дата релиза фильма должна совпадать с ожидаемой")
                .isEqualTo(testFilm.getReleaseDate());

        assertThat(foundFilm.getDuration())
                .withFailMessage("Продолжительность фильма должна совпадать с ожидаемой")
                .isEqualTo(testFilm.getDuration());
    }

    @Test
    void testGetFilm_WhenFilmNotFound() {
        assertThatThrownBy(() -> filmStorage.getFilm(999L))
                .withFailMessage("Должно быть выброшено исключение NotFoundException при поиске несуществующего фильма")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Не найден фильм с идентификатором 999");
    }

    @Test
    void testGetFilm_WhenFilmIdIsNull() {
        assertThatThrownBy(() -> filmStorage.getFilm(null))
                .withFailMessage("Должно быть выброшено исключение ValidationException при поиске фильма с null ID")
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Не указан идентификатор фильма");
    }

    @Test
    void testGetFilms() {
        filmStorage.addNewFilm(testFilm);
        Film secondFilm = Film.builder()
                .name("Белое солнце пустыни")
                .description("Отличный советский фильм")
                .releaseDate(LocalDate.of(1962, 10, 2))
                .duration(177)
                .ratingId(2L)
                .build();
        filmStorage.addNewFilm(secondFilm);

        Collection<Film> films = filmStorage.getFilms();

        assertThat(films)
                .withFailMessage("Список фильмов не должен быть null")
                .isNotNull();

        assertThat(films)
                .withFailMessage("Количество фильмов должно быть равно 2")
                .hasSize(2);
    }

    @Test
    void testDeleteFilm() {
        Film savedFilm = filmStorage.addNewFilm(testFilm);
        Long filmId = savedFilm.getId();

        filmStorage.deleteFilm(filmId);

        assertThatThrownBy(() -> filmStorage.getFilm(filmId))
                .withFailMessage("После удаления фильм не должен быть доступен")
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void testDeleteFilm_WhenFilmNotFound() {
        assertThatThrownBy(() -> filmStorage.deleteFilm(999L))
                .withFailMessage("Должно быть выброшено исключение NotFoundException при удалении несуществующего фильма")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Не найден фильм с идентификатором 999");
    }

    @Test
    void testDeleteFilm_WhenFilmIdIsNull() {
        assertThatThrownBy(() -> filmStorage.deleteFilm(null))
                .withFailMessage("Должно быть выброшено исключение ValidationException при удалении фильма с null ID")
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Не указан идентификатор фильма");
    }

    @Test
    void testAddLike() {
        Film savedFilm = filmStorage.addNewFilm(testFilm);
        User savedUser = userStorage.addNewUser(testUser);

        boolean result = filmStorage.addLike(savedFilm, savedUser.getId());

        assertThat(result)
                .withFailMessage("Метод должен вернуть true при успешном добавлении лайка")
                .isTrue();

        Film filmWithLikes = filmStorage.getFilm(savedFilm.getId());
        assertThat(filmWithLikes.getLikes())
                .withFailMessage("Список лайков должен содержать ID пользователя")
                .contains(savedUser.getId());
    }

    @Test
    void testDislike() {
        Film savedFilm = filmStorage.addNewFilm(testFilm);
        User savedUser = userStorage.addNewUser(testUser);
        filmStorage.addLike(savedFilm, savedUser.getId());
        boolean result = filmStorage.dislike(filmStorage.getFilm(savedFilm.getId()), savedUser.getId());

        assertThat(result)
                .withFailMessage("Метод должен вернуть true при успешном удалении лайка " + result)
                .isTrue();

        Film filmWithoutLikes = filmStorage.getFilm(savedFilm.getId());
        assertThat(filmWithoutLikes.getLikes())
                .withFailMessage("Список лайков не должен содержать ID пользователя после удаления")
                .doesNotContain(savedUser.getId());
    }
}
