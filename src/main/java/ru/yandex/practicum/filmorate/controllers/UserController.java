package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Id;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

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
    @PostMapping(value = "/users")
    public String createUser(@Valid @RequestBody User user) throws ValidationException {
        ValidationUser.checkLogin(user); // проверка логина пользователя
        if (user.getName().isEmpty()) { // если имя пользователя пустое, используется логин пользователя
            user.setName(user.getLogin());
        }
        ValidationUser.checkBirthDay(user); // проверка даты рождения пользователя
        user.setUserId(Id.getId(users.keySet())); // сгенерировали id
        log.info("Пользователь {} успешно добавлен", user.getLogin());
        users.put(user.getUserId(), user);
        return "Пользователь " + user.getName() + " успешно добавлен";
    }

    // обновление пользователя
    @PutMapping(value = "/users")
    public String updateUser(@Valid @RequestBody User user) throws ValidationException {

        if (users.containsKey(user.getUserId())) {
            User updUser = users.get(user.getUserId());
            ValidationUser.checkLogin(user); // проверка логина пользователя
            updUser.setName(user.getName()); // Обновили логина пользователя
            updUser.setEmail(user.getEmail()); // обновили email
            ValidationUser.checkBirthDay(user); // проверка даты рождения пользователя
            updUser.setBirthday(user.getBirthday()); // обновили дату рождения
            users.put(updUser.getUserId(), updUser); // положили в таблицу обновлённые данные
            log.info("Данные пользователя {} успешно обновлены", user);
            return "Данные пользователя " + user.getName() + " успешно обновлены";
        } else {
            log.warn("Введён неверный id");
            throw new ValidationException("пользователя с ID " + user.getUserId() + " нет");
        }
    }

    // получение всех пользователей
    @GetMapping("/users")
    public Map<Integer, User> getAllUsers() {
        return users;
    }
}
