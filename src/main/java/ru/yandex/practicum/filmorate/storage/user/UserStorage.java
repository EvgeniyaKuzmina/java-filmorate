package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

//методы добавления, удаления и модификации объектов.
public interface UserStorage {

    // метод для создания пользователя
    String createUser(User user) throws ValidationException;

    // метод для изменения данных пользователя
    String updateUser(User user) throws ValidationException;

    // метод для получения списка всех пользователей
    Map<Integer, User> getAllUsers();

    // метод удаления пользователя
    String removeUser(User user);
}
