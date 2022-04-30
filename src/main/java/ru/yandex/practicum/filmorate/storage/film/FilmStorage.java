package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    // метод для добавления фильма
    String createFilm(Film film) throws ValidationException;

    // метод для изменения данных о фильме
    String updateFilm(Film film) throws ValidationException;

    // метод для получения списка всех фильмов
    Map<Integer, Film> getAllFilms();

    // метод удаления фильма
    String removeFilm(Film film);
}
