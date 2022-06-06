package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
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
    String removeUser(Long id);

    // получение пользователя по id
    User getUsersById(Long id);

    Map<Long, User> getUsers();

    void setUsers(long id, User newUser);

    // добавление пользователя в друзья
    String addFriend(Long userId, Long friendId);

    // удаление из друзей
    String removeFriend(Long userId, Long friendId);

    // получение списка общих друзей
    List<User> getCommonFriends(Long userId, Long otherUserId) throws UserNotFoundException;

    // получение списка друзей пользователя
    List<User> getUserFriendById(Long id);

    //void users(Long id, User user);
}
