package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.List;

@Slf4j
@Component
@Qualifier("FilmDbService")
public class FilmDbService {

    private final FilmStorage filmStorage;

    public FilmDbService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    // проверка введёных пользователем данных
    private Film checkFilm(Film film) throws ValidationException {
        ValidationFilm.checkDataOfRelease(film); // проверка даты релиза фильма
        ValidationFilm.checkDuration(film); // проверка продолжительности фильма
        return film;
    }

    // создание фильма
    public Film createFilm(Film film) throws ValidationException {
        Film newFilm = checkFilm(film); // проверяем что введённые данные валидные
        return filmStorage.createFilm(newFilm);
    }

    // обновление фильма
    public Film updateFilm(Film film) throws ValidationException {
        Film updFilm = checkFilm(film); // проверяем что введённые данные валидные
        return filmStorage.updateFilm(updFilm);
    }

    // получение списка всех фильмов
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    // удаление фильма
    public String removeFilm(Long id) {
        return filmStorage.removeFilm(id);
    }

    // получение фильма по id
    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    //добавление лайка
    public String addLike(Long filmId, Long userId) {
        return filmStorage.addLike(filmId, userId);
    }

    // удаление лайка
    public String removeLike(Long filmId, Long userId) {
        return filmStorage.removeLike(filmId, userId);
    }

    // вывод наиболее популярных фильмов по количеству лайков.
    public List<Film> mostPopularFilm(Long count) {
        return filmStorage.mostPopularFilm(count);
    }


}
