package ru.yandex.practicum.filmorate.exceptions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationExceptionFilmTest {
    private static final LocalDate EARLIEST_DATA_OF_RELEASE = LocalDate.of(1895, 12, 28);
    private static Film film;


    @BeforeAll
    static void createFilm() {
        film = new Film("", "Очень очень длинное описание фильма -------------------------------------" +
                "---------------------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------",
                        LocalDate.of(1895, 12, 27), Duration.ofHours(-10));

    }

    @Test
    void checkDescriptionFilmNotBeMoreLength200() {
        try {
            ValidationExceptionFilm.checkDescription(film);
        } catch (ValidationException e) {
            assertEquals("В описании фильма " + film.getDescription().length() + " символов." +
                                 " Допустимое количество символом в описании 200", e.getMessage());
        }
    }

    @Test
    void checkDataOfReleaseFilmNotBeEarlier28_12_1895() {
        try {
            ValidationExceptionFilm.checkDataOfRelease(film);
        } catch (ValidationException e) {
            assertEquals("Вы не можете указать дату релиза ранее " + EARLIEST_DATA_OF_RELEASE, e.getMessage());
        }
    }

    @Test
    void checkDurationFilmNotLess0() {
        try {
            ValidationExceptionFilm.checkDuration(film);
        } catch (ValidationException e) {
            assertEquals("Продолжительности фильма не может быть отрицательной.", e.getMessage());
        }
    }

    @Test
    void checkNameForFilmNotBeEmpty() {
        film = new Film("", "Описание фильма 1", LocalDate.of(2022, 02, 25), Duration.ofHours(10));
        try {
            ValidationExceptionFilm.checkName(film);
        } catch (ValidationException e) {
            assertEquals("Название фильма не может быть пустым", e.getMessage());
        }
    }


}