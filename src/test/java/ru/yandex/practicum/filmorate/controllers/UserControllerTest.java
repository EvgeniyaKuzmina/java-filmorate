package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserControllerTest {
    UserController userController = new UserController();
    private User user;

    // проверяем что пользователь с датой рождения позднее текущей не будет создан
    @Test
    void shouldNotCreateUserWithBirthDayLateToday() {
        user = new User("test@ya.ru", "Логин", "", LocalDate.of(2023, 1, 1));
        String result = userController.createUser(user);
        assertEquals(new HashMap<>(), userController.getUsers());
        assertEquals("Дата рождения указан не корректно", result);
    }

    // проверяем что пользователь с датой рождения сегодня будет создан
    @Test
    void shouldCreateUserWithBirthDayToday() {
        user = new User("test@ya.ru", "Логин", "", LocalDate.of(2022, 4, 18));
        String result = userController.createUser(user);
        assertFalse(userController.getUsers().isEmpty());
        assertEquals("Пользователь " + user.getLogin() + " успешно добавлен", result);
    }

    // проверяем что пользователь с пустым логином не будет создан
    @Test
    void shouldNotCreateUserWithEmptyLogin() {
        user = new User("test@ya.ru", "", "", LocalDate.of(2022, 1, 1));
        String result = userController.createUser(user);
        assertEquals(new HashMap<>(), userController.getUsers());
        assertEquals("Логин пользователя не может быть пустым", result);
    }

    // проверяем что пользователь не будет создан, если вместо логина указаны пробелы
    @Test
    void shouldNotCreateUserWithBlancLogin() {
        user = new User("test@ya.ru", "  ", "", LocalDate.of(2022, 1, 1));
        String result = userController.createUser(user);
        assertEquals(new HashMap<>(), userController.getUsers());
        assertEquals("Логин пользователя не может быть пустым", result);
    }

    //проверяем что пользователь будет создан, если имя пустое, но указан логин. В имени пользователя при этом будет указан логин
    @Test
    void shouldCreateUserWithEmptyName() {
        user = new User("test@ya.ru", "Логин", "", LocalDate.of(2022, 1, 1));
        String result = userController.createUser(user);
        assertFalse(userController.getUsers().isEmpty());
        assertEquals(user.getUserName(), user.getLogin());
        assertEquals("Пользователь " + user.getLogin() + " успешно добавлен", result);
    }

    // проверяем что без ID обновить данные пользователя не получится
    @Test
    void shouldNotUpdateUserWithoutID() {
        user = new User("test@ya.ru", "Логин", "", LocalDate.of(2022, 1, 1));
        userController.createUser(user);
        Map<Integer, User> userWithOldInformation = userController.getUsers();
        user = new User("test@ya.ru", "Логин", "", LocalDate.of(2022, 1, 1));
        String result = userController.updateUser(user);
        assertEquals(userWithOldInformation, userController.getUsers());
        assertEquals("пользователя с ID " + user.getUserId() + " нет", result);
    }

    // проверяем что получаем список всех пользователей
    @Test
    void shouldGetAllUsers() {
        user = new User("test@ya.ru", "Логин 1", "Пользователь 2", LocalDate.of(2022, 1, 1));
        userController.createUser(user);
        User user2 = new User("test@ya.ru", "Логин 2", "Пользователь 2", LocalDate.of(2022, 1, 1));
        userController.createUser(user2);
        assertEquals(1, user.getUserId());
        assertEquals(2, user2.getUserId());
        assertFalse(userController.getAllUsers().isEmpty());

    }
}