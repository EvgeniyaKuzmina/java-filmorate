package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

@Slf4j
@Service

public class ValidationUser {


    private final UserStorage userStorage;

    public ValidationUser(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
            throw new ValidationException("Логин пользователя не может содержать пробелы");
        }
    }

    public void checkUserById(Long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, id));
        }
    }


}
