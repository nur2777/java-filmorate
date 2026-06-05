package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    @Test
    void testWhenCorrectFilm() {
        Film testFilm = Film.builder()
                .name("Интерстеллар")
                .description("Описание")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(2)
                .build();
        assertDoesNotThrow(() -> {Film.filmChecks(testFilm);});
    }

    @Test
    void testWhenFilmNameEmpty() {
        Film testFilm = Film.builder()
                .description("Описание")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(2)
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> Film.filmChecks(testFilm));
        assertEquals("Название не может быть пустым", exception.getMessage(),"Неверный текст ошибки!");
    }

    @Test
    void testWhenFilmNameIsBlank() {
        Film testFilm = Film.builder()
                .description("Описание")
                .name("    ")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(2)
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> Film.filmChecks(testFilm));
        assertEquals("Название не может быть пустым", exception.getMessage(),"Неверный текст ошибки!");
    }

    @Test
    void testWhenDescriptionLength250() {
        Film testFilm = Film.builder()
                .description("Описание ".repeat(30))
                .name("Интерстеллар")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(2)
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> Film.filmChecks(testFilm));
        assertEquals("Длина описания должна быть максимум 200 символов", exception.getMessage(),
                "Неверный текст ошибки!");
    }


    @Test
    void testWhenErrorReleaseDate() {
        Film testFilm = Film.builder()
                .description("описание ")
                .name("Интерстеллар")
                .releaseDate(LocalDate.of(1895,12,27))
                .duration(2)
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> Film.filmChecks(testFilm));
        assertEquals("Дата релиза должна быть не ранее 28.12.1895", exception.getMessage(),
                "Неверный текст ошибки!");
    }

    @Test
    void testWhenNegativeDuration() {
        Film testFilm = Film.builder()
                .description("описание ")
                .name("Интерстеллар")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(-2)
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> Film.filmChecks(testFilm));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage(),
                "Неверный текст ошибки!");
    }
}