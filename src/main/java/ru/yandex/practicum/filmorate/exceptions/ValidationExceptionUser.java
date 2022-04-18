package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ValidationExceptionUser extends ValidationException {

    public ValidationExceptionUser(String messages) {
        super(messages);
    }

    // проверка даты рождения пользователя
    public static void checkBirthDay(User user) throws ValidationException {
        if (user.getBirthDay().isAfter(LocalDate.now())) {
            log.warn("Дата рождения указана позднее текущей даты");
            throw new ValidationException("Дата рождения указан не корректно");
        }
    }
    //проверка email адреса
    public static void checkEmial(User user) throws ValidationException {
        if (!user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.warn("Дата рождения указана позднее текущей даты");
            throw new ValidationException("Дата рождения указан не корректно");
        }
    }
}
