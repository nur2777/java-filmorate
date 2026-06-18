package ru.yandex.practicum.filmorate.model;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTest {
    private static final String hostURL = "http://localhost:8080";
    private static HttpClient client;
    /**
     * Gson-объект
     */
    public static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                    (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                    (json, typeOfT, context) ->
                            LocalDate.parse(json.getAsString())).setPrettyPrinting().create();

    @BeforeAll
    static void beforeAll() throws Exception {
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    }

    private static HttpResponse<String> sendPostToUsers(User testUser) throws IOException, InterruptedException {
        String userBody = gson.toJson(testUser);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(hostURL + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userBody))
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    @Test
    void testWhenCorrectUser() throws IOException, InterruptedException {
        User testUser = User.builder()
                .email("test@test.com")
                .login("login")
                .name("Sam")
                .birthday(LocalDate.of(2002, 5, 19))
                .build();
        HttpResponse<String> response = sendPostToUsers(testUser);
        assertEquals(200, response.statusCode(), "Должен вернуться код 200 при корректном пользователе!");
    }

    @Test
    void testWhenUserEmailEmpty() throws IOException, InterruptedException {
        User testUser = User.builder()
                .login("login")
                .name("Sam")
                .birthday(LocalDate.of(2002, 5, 19))
                .build();
        HttpResponse<String> response = sendPostToUsers(testUser);
        assertEquals(400, response.statusCode(), "Должна вернуться ошибка 400 при пустом email!");
    }

    @Test
    void testWhenUserIncorrectEmailFormat() throws IOException, InterruptedException {
        User testUser = User.builder()
                .login("login")
                .email("testtest.com")
                .name("Sam")
                .birthday(LocalDate.of(2002, 5, 19))
                .build();
        HttpResponse<String> response = sendPostToUsers(testUser);
        assertEquals(400, response.statusCode(), "Должна вернуться ошибка 400 при невалидном email!");
    }

    @Test
    void testWhenUserLoginEmpty() throws IOException, InterruptedException {
        User testUser = User.builder()
                .name("Sam")
                .email("test@test.com")
                .birthday(LocalDate.of(2002, 5, 19))
                .build();
        HttpResponse<String> response = sendPostToUsers(testUser);
        assertEquals(400, response.statusCode(), "Должна вернуться ошибка 400 при пустом логине!");
    }

    @Test
    void testWhenUserLoginContainSpace() throws IOException, InterruptedException {
        User testUser = User.builder()
                .login("log  in")
                .email("test@test.com")
                .name("Sam")
                .birthday(LocalDate.of(2002, 5, 19))
                .build();
        HttpResponse<String> response = sendPostToUsers(testUser);
        assertEquals(400, response.statusCode(), "Должна вернуться ошибка 400 при логине c пробелом!");
    }

    @Test
    void testWhenUserBirthdayInFuture() throws IOException, InterruptedException {
        User testUser = User.builder()
                .login("login")
                .email("test@test.com")
                .name("Sam")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        HttpResponse<String> response = sendPostToUsers(testUser);
        assertEquals(400, response.statusCode(), "Должна вернуться ошибка 400 при дате рождения " +
                "в будущем!");
    }

    @Test
    void testWhenUserNameEmpty() {
        User testUser = User.builder()
                .login("login")
                .email("test@test.com")
                .birthday(LocalDate.of(2002, 5, 19))
                .build();

        assertEquals(testUser.getLogin(), testUser.getName(), "При заданном пустом имени должен возвращаться " +
                "логин");
    }
}
