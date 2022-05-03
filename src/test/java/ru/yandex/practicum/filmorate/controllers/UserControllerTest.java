package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserControllerTest {
    private InMemoryUserStorage inMemoryUserStorage;
    private User user;

    @Autowired
    public UserControllerTest(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    // проверяем что пользователь с датой рождения позднее текущей не будет создан
    @Test
    void shouldNotCreateUserWithBirthDayLateToday() {
        try {
            user = new User("test@ya.ru", "Логин", "", LocalDate.of(2023, 1, 1));
            String result = inMemoryUserStorage.createUser(user);
            assertEquals(new HashMap<>(), inMemoryUserStorage.getUsers());
            assertEquals("Дата рождения указан не корректно", result);
        } catch (ValidationException e) {
            e.getMessage();
        }
    }

    // проверяем что пользователь с датой рождения сегодня будет создан
    @Test
    void shouldCreateUserWithBirthDayToday() {
        try {
            user = new User("test@ya.ru", "Логин", "", LocalDate.of(2022, 4, 18));
            String result = inMemoryUserStorage.createUser(user);
            assertFalse(inMemoryUserStorage.getUsers().isEmpty());
            assertEquals("Пользователь " + user.getLogin() + " успешно добавлен", result);
        } catch (ValidationException e) {
            e.getMessage();
        }
    }

    // проверяем что пользователь с пустым логином не будет создан
    @Test
    void shouldNotCreateUserWithEmptyLogin() {
        try {
            user = new User("test@ya.ru", "", "", LocalDate.of(2022, 1, 1));
            String result = inMemoryUserStorage.createUser(user);
            assertEquals(new HashMap<>(), inMemoryUserStorage.getUsers());
            assertEquals("Логин пользователя не может быть пустым", result);
        } catch (ValidationException e) {
            e.getMessage();
        }
    }

    // проверяем что пользователь не будет создан, если вместо логина указаны пробелы
    @Test
    void shouldNotCreateUserWithBlancLogin() {
        try {
            user = new User("test@ya.ru", "  ", "", LocalDate.of(2022, 1, 1));
            String result = inMemoryUserStorage.createUser(user);
            assertEquals(new HashMap<>(), inMemoryUserStorage.getUsers());
            assertEquals("Логин пользователя не может быть пустым", result);
        } catch (ValidationException e) {
            e.getMessage();
        }
    }

    //проверяем что пользователь будет создан, если имя пустое, но указан логин. В имени пользователя при этом будет указан логин
    @Test
    void shouldCreateUserWithEmptyName() {
        try {
            user = new User("test@ya.ru", "Логин", "", LocalDate.of(2022, 1, 1));
            String result = inMemoryUserStorage.createUser(user);
            assertFalse(inMemoryUserStorage.getUsers().isEmpty());
            assertEquals(user.getName(), user.getLogin());
            assertEquals("Пользователь " + user.getLogin() + " успешно добавлен", result);
        } catch (ValidationException e) {
            e.getMessage();
        }
    }

    // проверяем что без ID обновить данные пользователя не получится
    @Test
    void shouldNotUpdateUserWithoutID() {
        try {
            user = new User("test@ya.ru", "Логин", "", LocalDate.of(2022, 1, 1));
            inMemoryUserStorage.createUser(user);
            Map<Integer, User> userWithOldInformation = inMemoryUserStorage.getUsers();
            user = new User("test@ya.ru", "Логин", "", LocalDate.of(2022, 1, 1));
            String result = inMemoryUserStorage.updateUser(user);
            assertEquals(userWithOldInformation, inMemoryUserStorage.getUsers());
            assertEquals("пользователя с ID " + user.getUserId() + " нет", result);
        } catch (ValidationException e) {
            e.getMessage();
        }
    }

    // проверяем что получаем список всех пользователей
    @Test
    void shouldGetAllUsers() {
        try {
            user = new User("test@ya.ru", "Логин 1", "Пользователь 2", LocalDate.of(2022, 1, 1));
            inMemoryUserStorage.createUser(user);
            User user2 = new User("test@ya.ru", "Логин 2", "Пользователь 2", LocalDate.of(2022, 1, 1));
            inMemoryUserStorage.createUser(user2);
            assertEquals(1, user.getUserId());
            assertEquals(2, user2.getUserId());
            assertFalse(inMemoryUserStorage.getAllUsers().isEmpty());
        } catch (ValidationException e) {
            e.getMessage();
        }

    }
}