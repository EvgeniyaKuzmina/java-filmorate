package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ValidationUser {

    // проверка даты рождения пользователя
    public static void checkBirthDay(User user) throws ValidationException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения указана позднее текущей даты");
            throw new ValidationException("Дата рождения указан не корректно");
        }
    }

    //проверка логина пользователя
    public static void checkLogin(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя содержит пробелы");
            throw new ValidationException("Логин пользователя не может содержить пробелы");
        }

    }
}