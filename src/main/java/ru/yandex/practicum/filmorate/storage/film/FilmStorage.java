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
    public Film addNewFilm(Film newFilm);

    /** Метод обновления фильма
     * @param film данные обновляемого фильма
     * @return объект обновленного фильма
     */
    public Film updateFilm(Film film);

    /** Метод удаления фильма
     * @param id идентификатор фильма
     */
    public void deleteFilm(Long id);

    /** Метод получения списка всех фильмов
     * @return список фильмов
     */
    public Collection<Film> getFilms();
}
