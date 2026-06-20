package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

/**
 * Интерфейс в котором будут определены методы добавления, удаления и модификации фильмов.
 */
public interface FilmStorage {

    /** Метод добавления фильма
     * @param newFilm данные нового фильма
     * @return объект нового фильма
     */
    Film addNewFilm(Film newFilm);

    /** Метод обновления фильма
     * @param film данные обновляемого фильма
     * @return объект обновленного фильма
     */
    Film updateFilm(Film film);

    /** Метод удаления фильма
     * @param id идентификатор фильма
     */
    void deleteFilm(Long id);

    /** Метод получения списка всех фильмов
     * @return список фильмов
     */
    Collection<Film> getFilms();

    /** Метод проверки существования и получения одного фильма по идентификатору
     * @return объект фильма
     */
    Film getFilm(Long id);
}
