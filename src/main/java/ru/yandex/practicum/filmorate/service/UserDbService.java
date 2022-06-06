package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.List;

@Slf4j
@Component
@Qualifier("UserDbService")
public class UserDbService {
    private final UserStorage userStorage;

    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // проверяем что переданные пользователем данные корректные
    private User checkUser(User user) throws ValidationException {
        ValidationUser.checkLogin(user); // проверка логина пользователя
        if (user.getName().isEmpty()) { // если имя пользователя пустое, используется логин пользователя
            user.setName(user.getLogin());
        }
        ValidationUser.checkBirthDay(user); // проверка даты рождения пользователя
        return user;
    }


    // создание пользователя
    public User createUser(User user) throws ValidationException {
        User newUser = checkUser(user); // проверяем что введённые данные валидные
        return userStorage.createUser(newUser);

    }

    // обновление пользователя
    public User updateUser(User user) throws ValidationException {
        User updUser = checkUser(user); // проверяем что введённые данные валидные
        return userStorage.updateUser(updUser);
    }

    // получение всех пользователей
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    // удаление пользователя по id
    public String removeUser(Long id) {
        return userStorage.removeUser(id);
    }

    // получение пользователя по id
    public User getUsersById(Long id) {
        return userStorage.getUsersById(id);
    }

    //добавление в друзья
    public String addFriend(Long userId, Long friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    // удаление из друзей
    public String removeFriend(Long userId, Long friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    // вывод списка общих друзей
    public List<User> getCommonFriends(Long userId, Long otherUserId) throws UserNotFoundException {
        return userStorage.getCommonFriends(userId, otherUserId);
    }

    // получение списка друзей пользователя по id
    public List<User> getUserFriendById(Long id) {
        return userStorage.getUserFriendById(id);
    }
}
