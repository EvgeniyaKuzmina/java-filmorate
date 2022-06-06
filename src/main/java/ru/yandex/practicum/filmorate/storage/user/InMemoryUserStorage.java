package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Getter
@Setter
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    // метод для создания пользователя
    @Override
    public User createUser(User user) throws ValidationException {
        ValidationUser.checkLogin(user); // проверка логина пользователя
        if (user.getName().isEmpty()) { // если имя пользователя пустое, используется логин пользователя
            user.setName(user.getLogin());
        }
        ValidationUser.checkBirthDay(user); // проверка даты рождения пользователя
        return user;
    }


    // метод для изменения данных пользователя
    @Override
    public User updateUser(User user) throws ValidationException {
            User updUser = users.get(user.getId());
            ValidationUser.checkLogin(user); // проверка логина пользователя
            updUser.setLogin(user.getLogin()); // Обновили логин пользователя
            updUser.setName(user.getName()); // Обновили имя пользователя
            updUser.setEmail(user.getEmail()); // обновили email
            ValidationUser.checkBirthDay(user); // проверка даты рождения пользователя
            updUser.setBirthday(user.getBirthday()); // обновили дату рождения
            users.put(updUser.getId(), updUser); // положили в таблицу обновлённые данные
            log.info("Данные пользователя {} успешно обновлены", user);
            return updUser;
    }

    // метод для получения списка всех пользователей
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // метод для получения пользователя по id
    @Override
    public User getUsersById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, id));
        }
        return users.get(id);

    }

    @Override
    public void setUsers(long id, User user) {
        users.put(id, user);
    }

    @Override
    public String addFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public String removeFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<User> getUserFriendById(Long id) {
        return null;
    }

    // метод удаления пользователя
    @Override
    public String removeUser(Long id) {
        return null;
    }


}
