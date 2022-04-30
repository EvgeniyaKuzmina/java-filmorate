package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationUser {

    private final InMemoryUserStorage inMemoryUserStorage;

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

    public void checkUserById(Integer id) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя c id %d нет", id));
        }
    }


}
