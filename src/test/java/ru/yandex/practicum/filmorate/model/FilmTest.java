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
class FilmTest {
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

    private static HttpResponse<String> sendPostToFilms(Film testFilm) throws IOException, InterruptedException {
        String userBody = gson.toJson(testFilm);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(hostURL + "/films"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userBody))
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    @Test
    void testWhenCorrectFilm() throws IOException, InterruptedException {
        Film testFilm = Film.builder()
                .name("Интерстеллар")
                .description("Описание")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(2)
                .build();
        HttpResponse<String> response = sendPostToFilms(testFilm);
        assertEquals(200, response.statusCode(), "Должен вернуться код 200 при корректном фильме!");
    }

    @Test
    void testWhenFilmNameEmpty() throws IOException, InterruptedException {
        Film testFilm = Film.builder()
                .description("Описание")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(2)
                .build();
        HttpResponse<String> response = sendPostToFilms(testFilm);
        assertEquals(400, response.statusCode(), "Должен вернуться код 400 при пустом названии фильма!");
    }

    @Test
    void testWhenFilmNameIsBlank() throws IOException, InterruptedException {
        Film testFilm = Film.builder()
                .description("Описание")
                .name("    ")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(2)
                .build();
        HttpResponse<String> response = sendPostToFilms(testFilm);
        assertEquals(400, response.statusCode(), "Должен вернуться код 400 при пробелах в " +
                "названии фильма!");
    }

    @Test
    void testWhenDescriptionLength250() throws IOException, InterruptedException {
        Film testFilm = Film.builder()
                .description("Описание ".repeat(30))
                .name("Интерстеллар")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(2)
                .build();
        HttpResponse<String> response = sendPostToFilms(testFilm);
        assertEquals(400, response.statusCode(), "Должен вернуться код 400 при длине " +
                "описания более 200 символов!");
    }


    @Test
    void testWhenErrorReleaseDate() throws IOException, InterruptedException {
        Film testFilm = Film.builder()
                .description("описание ")
                .name("Интерстеллар")
                .releaseDate(LocalDate.of(1895,12,27))
                .duration(2)
                .build();
        HttpResponse<String> response = sendPostToFilms(testFilm);
        assertEquals(400, response.statusCode(), "Должен вернуться код 400 при дате релиза " +
                "ранее 28.12.1895 ");
    }

    @Test
    void testWhenNegativeDuration() throws IOException, InterruptedException {
        Film testFilm = Film.builder()
                .description("описание ")
                .name("Интерстеллар")
                .releaseDate(LocalDate.of(2014,11,5))
                .duration(-2)
                .build();
        HttpResponse<String> response = sendPostToFilms(testFilm);
        assertEquals(400, response.statusCode(), "Должен вернуться код 500 при отрицательной " +
                "продолжительности фильма");
    }
}