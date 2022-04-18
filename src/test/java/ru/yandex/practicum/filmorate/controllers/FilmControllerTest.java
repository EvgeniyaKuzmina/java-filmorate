package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FilmControllerTest {
    private static final LocalDate EARLIEST_DATA_OF_RELEASE = LocalDate.of(1895, 12, 28);
    FilmController filmController = new FilmController();
    private Film film;

    // проверяем что фильм с пустым названием не будет добавлен
    @Test
    void shouldNotCreateFilmWithEmptyName() {
        film = new Film("", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------",
                        LocalDate.of(1895, 12, 29), Duration.ofHours(10));
        String result = filmController.createFilm(film);
        assertEquals(new HashMap<>(), filmController.getFilms());
        assertEquals("Название фильма не может быть пустым", result);

    }

    // проверяем что фильм с пробелами вместо названия не будет добавлен
    @Test
    void shouldNotCreateFilmWithBlancName() {
        film = new Film("   ", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------",
                        LocalDate.of(1895, 12, 29), Duration.ofHours(10));
        String result = filmController.createFilm(film);
        assertEquals(new HashMap<>(), filmController.getFilms());
        assertEquals("Название фильма не может быть пустым", result);

    }

    // проверяем что фильм с описанием более 200 символов не будет добавлен
    @Test
    void shouldNotCreateFilmDescriptionLengthMore200() {
        film = new Film("Название фильма", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------",
                        LocalDate.of(1895, 12, 29), Duration.ofHours(10));
        String result = filmController.createFilm(film);
        assertEquals(new HashMap<>(), filmController.getFilms());
        assertEquals("В описании фильма " + film.getDescription().length() + " символов." +
                             " Допустимое количество символом в описании 200", result);

    }

    // проверяем что фильм с описанием в 200 символов будет добавлен
    @Test
    void shouldCreateFilmDescriptionLength200() {
        film = new Film("Название фильма", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "----------------------------------",
                        LocalDate.of(1895, 12, 29), Duration.ofHours(10));
        String result = filmController.createFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
        assertEquals("Фильм " + film.getFilmName() + " успешно добавлен", result);

    }

    // проверяем что фильм датой релиза ранее 28.12.1895 не будет добавлен
    @Test
    void shouldNotCreateFilmWithDataRealiseEarlier28_12_1895() {
        film = new Film("Название фильма", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "----------------------------------",
                        LocalDate.of(1895, 12, 27), Duration.ofHours(10));
        String result = filmController.createFilm(film);
        assertEquals(new HashMap<>(), filmController.getFilms());
        assertEquals("Вы не можете указать дату релиза ранее " + EARLIEST_DATA_OF_RELEASE, result);

    }

    // проверяем что фильм датой релиза 28.12.1895  будет добавлен
    @Test
    void shouldCreateFilmWithDataRealise28_12_1895() {
        film = new Film("Название фильма", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "----------------------------------",
                        LocalDate.of(1895, 12, 28), Duration.ofHours(10));
        String result = filmController.createFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
        assertEquals("Фильм " + film.getFilmName() + " успешно добавлен", result);

    }

    // проверяем что фильм с отрицательной продолжительностью не будет добавлен
    @Test
    void shouldNotCreateFilmWithDurationLess0() {
        film = new Film("Название фильма", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "----------------------------------",
                        LocalDate.of(1895, 12, 28), Duration.ofHours(-1));
        String result = filmController.createFilm(film);
        assertEquals(new HashMap<>(), filmController.getFilms());
        assertEquals("Продолжительности фильма не может быть отрицательной.", result);

    }

    // проверяем что фильм с продолжительностью 0 будет добавлен
    @Test
    void shouldCreateFilmWithDuration0() {
        film = new Film("Название фильма", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "----------------------------------",
                        LocalDate.of(1895, 12, 28), Duration.ofHours(0));
        String result = filmController.createFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
        assertEquals("Фильм " + film.getFilmName() + " успешно добавлен", result);

    }

    // проверяем что без ID обновить фильм не получится
    @Test
    void shouldNotUpdateFilmWithoutID() {
        film = new Film("Название фильма", "Старое описание фильма",
                        LocalDate.of(1895, 12, 28), Duration.ofHours(0));
        filmController.createFilm(film);
        Map<Integer, Film> filmWithOldInformation = filmController.getFilms();
        film = new Film("Название фильма", "Новое описание фильма",
                        LocalDate.of(1895, 12, 28), Duration.ofHours(0));
        String result = filmController.updateFilm(film);
        assertEquals(filmWithOldInformation, filmController.getFilms());
        assertEquals("Фильма с ID " + film.getFilmId() + " нет", result);
    }

    // проверяем что получаем список всех фильмов
    @Test
    void shouldGetAllFilms() {
        film = new Film("Название фильма", "Старое описание фильма",
                        LocalDate.of(1895, 12, 28), Duration.ofHours(0));
        filmController.createFilm(film);
        Film film2 = new Film("Название фильма", "",
                              LocalDate.of(1895, 12, 28), Duration.ofHours(0));

        filmController.createFilm(film2);
        assertEquals(1, film.getFilmId());
        assertEquals(2, film2.getFilmId());
        assertFalse(filmController.getAllFilms().isEmpty());

    }
}