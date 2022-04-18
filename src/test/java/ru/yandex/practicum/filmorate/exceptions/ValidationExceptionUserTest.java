package ru.yandex.practicum.filmorate.exceptions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationExceptionUserTest {
    private static User user;

    @BeforeAll
    static void createUser() {
        user = new User("test@ya.ru", "", "", LocalDate.of(2023, 1, 1));
    }

    @Test
    void birthDayNotBeLateToday() {
        try {
            ValidationExceptionUser.checkBirthDay(user);
        } catch (ValidationException e) {
            assertEquals("Дата рождения указан не корректно", e.getMessage());
        }
    }

}