package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // создание пользователя
    @PostMapping(value = "/users")
    public String createUser(@Valid @RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.createUser(user);
    }

    // обновление пользователя
    @PutMapping(value = "/users")
    public String updateUser(@Valid @RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.updateUser(user);
    }

    // получение всех пользователей
    @GetMapping("/users")
    public Map<Integer, User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    // добавление в друзья addFriend
}
