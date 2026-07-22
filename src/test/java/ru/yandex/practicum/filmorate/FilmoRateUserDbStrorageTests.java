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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(UserDbStorage.class)
class FilmoRateUserDbStrorageTests {

    @Autowired
    private UserDbStorage userStorage;

    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        // Первый тестовый пользователь
        testUser = User.builder()
                .email("nradmir@gmail.com")
                .login("nradmir")
                .name("Radmir")
                .birthday(LocalDate.of(1990, 7, 12))
                .build();
        userStorage.addNewUser(testUser);

        // Второй тестовый пользователь
        testUser2 = User.builder()
                .email("semen@gmail.com")
                .login("testSemen")
                .name("Semen")
                .birthday(LocalDate.of(1995, 1, 21))
                .build();
    }

    @Test
    public void testGetUser() {
        User savedUser = userStorage.addNewUser(testUser);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(savedUser.getId()));

        assertThat(userOptional)
                .isPresent()
                .withFailMessage("Сохраненный пользователь не получен по идентификатору " + savedUser.getId())
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", savedUser.getId())
                );
    }

    @Test
    void testAddNewUser() {
        User savedUser = userStorage.addNewUser(testUser);

        assertThat(savedUser)
                .withFailMessage("Сохраненный пользователь не должен быть null")
                .isNotNull();

        assertThat(savedUser.getId())
                .withFailMessage("ID сохраненного пользователя не должен быть null")
                .isNotNull();

        assertThat(savedUser.getEmail())
                .withFailMessage("Email пользователя не совпадает с ожидаемым")
                .isEqualTo(testUser.getEmail());

        assertThat(savedUser.getLogin())
                .withFailMessage("Login пользователя не совпадает с ожидаемым")
                .isEqualTo(testUser.getLogin());

        assertThat(savedUser.getName())
                .withFailMessage("Имя пользователя не совпадает с ожидаемым")
                .isEqualTo(testUser.getName());

        assertThat(savedUser.getBirthday())
                .withFailMessage("Дата рождения пользователя не совпадает с ожидаемой")
                .isEqualTo(testUser.getBirthday());

        assertThat(savedUser.getFriends())
                .withFailMessage("Список друзей должен быть пустым")
                .isEmpty();
    }

    @Test
    void testAddNewUser_WhenNameIsNull() {
        testUser.setName(null);

        User savedUser = userStorage.addNewUser(testUser);

        assertThat(savedUser)
                .withFailMessage("Сохраненный пользователь не должен быть null")
                .isNotNull();

        assertThat(savedUser.getName())
                .withFailMessage("Имя должно быть заменено на login, так как было null")
                .isEqualTo(testUser.getLogin());
    }

    @Test
    void testUpdateUser() {
        User savedUser = userStorage.addNewUser(testUser);
        savedUser.setName("Игорь");
        savedUser.setEmail("igor@gmail.com");
        savedUser.setLogin("igor");
        savedUser.setBirthday(LocalDate.of(1999, 12, 12));

        User updatedUser = userStorage.updateUser(savedUser);

        assertThat(updatedUser)
                .withFailMessage("Обновленный пользователь не должен быть null")
                .isNotNull();

        assertThat(updatedUser.getId())
                .withFailMessage("ID обновленного пользователя должен совпадать с исходным")
                .isEqualTo(savedUser.getId());

        assertThat(updatedUser.getName())
                .withFailMessage("Имя пользователя должно быть обновлено")
                .isEqualTo("Игорь");

        assertThat(updatedUser.getEmail())
                .withFailMessage("Email пользователя должен быть обновлен")
                .isEqualTo("igor@gmail.com");

        assertThat(updatedUser.getLogin())
                .withFailMessage("Login пользователя должен быть обновлен")
                .isEqualTo("igor");

        assertThat(updatedUser.getBirthday())
                .withFailMessage("Дата рождения пользователя должна быть обновлена")
                .isEqualTo(LocalDate.of(1999, 12, 12));
    }

    @Test
    void testGetUsers() {
        userStorage.addNewUser(testUser);
        userStorage.addNewUser(testUser2);

        Collection<User> users = userStorage.getUsers();

        assertThat(users)
                .withFailMessage("Список пользователей не должен быть null")
                .isNotNull();

        assertThat(users)
                .withFailMessage("Количество пользователей должно быть равно 3 " + users.size())
                .hasSize(3);
    }

    @Test
    void testDeleteUser() {
        User savedUser = userStorage.addNewUser(testUser);
        Long userId = savedUser.getId();

        userStorage.deleteUser(userId);

        assertThatThrownBy(() -> userStorage.getUser(userId))
                .withFailMessage("После удаления пользователь не должен быть доступен")
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void testDeleteUser_WhenUserNotFound() {
        assertThatThrownBy(() -> userStorage.deleteUser(999L))
                .withFailMessage("Должно быть выброшено исключение NotFoundException при удалении несуществующего пользователя")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Не найден пользователь с идентификатором 999");
    }

    @Test
    void testDeleteUser_WhenUserIdIsNull() {
        assertThatThrownBy(() -> userStorage.deleteUser(null))
                .withFailMessage("Должно быть выброшено исключение ValidationException при удалении пользователя с null ID")
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Не указан идентификатор пользователя");
    }

    @Test
    void testAddNewFriend() {
        User savedUser = userStorage.addNewUser(testUser);
        User savedFriend = userStorage.addNewUser(testUser2);

        boolean result = userStorage.addNewFriend(savedUser, savedFriend.getId());

        assertThat(result)
                .withFailMessage("Метод должен вернуть true при успешном добавлении друга")
                .isTrue();

        User userWithFriends = userStorage.getUser(savedUser.getId());
        assertThat(userWithFriends.getFriends())
                .withFailMessage("Список друзей должен содержать ID добавленного друга")
                .contains(savedFriend.getId());
    }

    @Test
    void testUnfriend() {
        User savedUser = userStorage.addNewUser(testUser);
        User savedFriend = userStorage.addNewUser(testUser2);
        userStorage.addNewFriend(savedUser, savedFriend.getId());

        boolean result = userStorage.unfriend(userStorage.getUser(savedUser.getId()), savedFriend.getId());

        assertThat(result)
                .withFailMessage("Метод должен вернуть true при успешном удалении друга")
                .isTrue();

        User userWithoutFriends = userStorage.getUser(savedUser.getId());
        assertThat(userWithoutFriends.getFriends())
                .withFailMessage("Список друзей должен быть пустым после удаления")
                .doesNotContain(savedFriend.getId());
    }

}
