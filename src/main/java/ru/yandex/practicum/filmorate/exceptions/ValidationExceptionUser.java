package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
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
}
