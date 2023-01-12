package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

//методы добавления, удаления и модификации объектов.
public interface UserStorage {

    // метод для создания пользователя
    User createUser(User user) throws ValidationException;

    // метод для изменения данных пользователя
    User updateUser(User user) throws ValidationException;

    // метод для получения списка всех пользователей
    List<User> getAllUsers();

    // метод удаления пользователя
    String removeUser(User user);

    // получение пользователя по id
    User getUsersById(Long id);

    Map<Long, User> getUsers();
}
