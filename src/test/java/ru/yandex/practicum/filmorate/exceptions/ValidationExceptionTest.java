package ru.yandex.practicum.filmorate.exceptions;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationExceptionTest {
    private Film film;
    private User user;


    @Test
    public void checkNameForFilmNotBeEmpty() {
        film = new Film("", "Описание фильма 1", LocalDate.of(2022, 02, 25), Duration.ofHours(10));
        try {
            ValidationException.checkName(film);
        } catch (ValidationException e) {
            assertEquals("Название фильма не может быть пустым", e.getMessage());
        }
    }

    @Test
    public void checkLoginForUserNotBeEmpty() {
        user = new User("test@ya.ru", "", "", LocalDate.of(2000, 01, 01));
        try {
            ValidationException.checkName(user);
        } catch (ValidationException e) {
            assertEquals("Логин пользователя не может быть пустым", e.getMessage());
        }
    }

}