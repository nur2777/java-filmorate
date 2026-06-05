package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testWhenCorrectUser() {
        User testUser = User.builder()
                        .email("test@test.com")
                        .login("login")
                        .name("Sam")
                        .birthday(LocalDate.of(2002,5,19))
                        .build();
        assertDoesNotThrow(() -> {User.userChecks(testUser);});
    }

    @Test
    void testWhenUserEmailEmpty() {
        User testUser = User.builder()
                .login("login")
                .name("Sam")
                .birthday(LocalDate.of(2002,5,19))
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> User.userChecks(testUser));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage(),"Неверный текст ошибки!");
    }

    @Test
    void testWhenUserIncorrectEmailFormat() {
        User testUser = User.builder()
                .login("login")
                .email("testtest.com")
                .name("Sam")
                .birthday(LocalDate.of(2002,5,19))
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> User.userChecks(testUser));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage(),"Неверный текст ошибки!");
    }

    @Test
    void testWhenUserLoginEmpty() {
        User testUser = User.builder()
                .name("Sam")
                .email("test@test.com")
                .birthday(LocalDate.of(2002,5,19))
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> User.userChecks(testUser));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage(),"Неверный текст ошибки!");
    }

    @Test
    void testWhenUserLoginContainSpace() {
        User testUser = User.builder()
                .login("log  in")
                .email("test@test.com")
                .name("Sam")
                .birthday(LocalDate.of(2002,5,19))
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> User.userChecks(testUser));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage(),"Неверный текст ошибки!");
    }

    @Test
    void testWhenUserBirthdayInFuture() {
        User testUser = User.builder()
                .login("login")
                .email("test@test.com")
                .name("Sam")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        ValidationException exception = assertThrows(ValidationException.class,() -> User.userChecks(testUser));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage(),"Неверный текст ошибки!");
    }

    @Test
    void testWhenUserNameEmpty() {
        User testUser = User.builder()
                .login("login")
                .email("test@test.com")
                .birthday(LocalDate.of(2002,5,19))
                .build();

        assertEquals(testUser.getLogin(),testUser.getName(),"При заданном пустом имени должен возвращаться " +
                "логин");
    }
}
