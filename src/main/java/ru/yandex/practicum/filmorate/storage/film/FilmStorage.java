package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    // метод для добавления фильма
    Film createFilm(Film film) throws ValidationException;

    // метод для изменения данных о фильме
    Film updateFilm(Film film) throws ValidationException;

    // метод для получения списка всех фильмов
    List<Film> getAllFilms();

    // метод удаления фильма
    String removeFilm(Film film);

    // получение фильма по id
    Film getFilmById(Integer id);

    Map<Integer, Film> getFilms();
}
