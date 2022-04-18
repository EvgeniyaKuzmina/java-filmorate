package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.ValidationExceptionUser;
import ru.yandex.practicum.filmorate.model.Id;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    public Map<Integer, User> getUsers() {
        return users;
    }

    // создание пользователя
    @PostMapping(value = "/user")
    public String createUser(@Valid @RequestBody User user) {
        try {
            ValidationException.checkName(user); // проверка логина пользователя
            if (user.getUserName().isEmpty()) { // если имя пользователя пустое, используется логин пользователя
                user.setUserName(user.getLogin());
            }
            ValidationExceptionUser.checkBirthDay(user); // проверка даты рождения пользователя
            user.setUserId(Id.getId(users.keySet())); // сгенерировали id
            log.info("Пользователь {} успешно добавлен", user.getLogin());
            users.put(user.getUserId(), user);
            return "Пользователь " + user.getUserName() + " успешно добавлен";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    // обновление пользователя
    @PutMapping(value = "/user")
    public String updateUser(@Valid @RequestBody User user) {
        try {
            if (users.containsKey(user.getUserId())) {
                User updUser = users.get(user.getUserId());
                ValidationException.checkName(user); // проверка логина пользователя
                updUser.setUserName(user.getUserName()); // Обновили логина пользователя
                updUser.setEmail(user.getEmail()); // обновили email
                ValidationExceptionUser.checkBirthDay(user); // проверка даты рождения пользователя
                updUser.setBirthDay(user.getBirthDay()); // обновили дату рождения
                users.put(updUser.getUserId(), updUser); // положили в таблицу обновлённые данные
                log.info("Данные пользователя {} успешно обновлены", user);
                return "Данные пользователя " + user.getUserName() + " успешно обновлены";
            } else {
                log.warn("Введён неверный id");
                throw new ValidationException("пользователя с ID " + user.getUserId() + " нет");
            }
        } catch (ValidationException e) {
            return e.getMessage();
        }

    }

    // получение всех пользователей
    @GetMapping("/users")
    public Map<Integer, User> getAllUsers() {
        return users;
    }
}
