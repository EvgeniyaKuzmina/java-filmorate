package ru.yandex.practicum.filmorate.exceptions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationFilmTest {
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
    void checkDataOfReleaseFilmNotBeEarlier28_12_1895() {
        try {
            ValidationFilm.checkDataOfRelease(film);
        } catch (ValidationException e) {
            assertEquals("Вы не можете указать дату релиза ранее " + EARLIEST_DATA_OF_RELEASE, e.getMessage());
        }
    }

    @Test
    void checkDurationFilmNotLess0() {
        try {
            ValidationFilm.checkDuration(film);
        } catch (ValidationException e) {
            assertEquals("Продолжительности фильма не может быть отрицательной.", e.getMessage());
        }
    }



}