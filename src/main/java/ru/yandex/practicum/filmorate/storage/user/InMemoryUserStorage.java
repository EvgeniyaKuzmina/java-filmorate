package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Id;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Getter
@Setter
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    // метод для создания пользователя
    public String createUser(User user) throws ValidationException {
        ValidationUser.checkLogin(user); // проверка логина пользователя
        if (user.getName().isEmpty()) { // если имя пользователя пустое, используется логин пользователя
            user.setName(user.getLogin());
        }
        ValidationUser.checkBirthDay(user); // проверка даты рождения пользователя
        user.setId(Id.getId(users.keySet())); // сгенерировали id
        log.info("Пользователь {} успешно добавлен", user.getLogin());
        users.put(user.getId(), user);
        return "Пользователь " + user.getName() + " успешно добавлен";
    }

    // метод для изменения данных пользователя
    public String updateUser(User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            User updUser = users.get(user.getId());
            ValidationUser.checkLogin(user); // проверка логина пользователя
            updUser.setName(user.getName()); // Обновили логина пользователя
            updUser.setEmail(user.getEmail()); // обновили email
            ValidationUser.checkBirthDay(user); // проверка даты рождения пользователя
            updUser.setBirthday(user.getBirthday()); // обновили дату рождения
            users.put(updUser.getId(), updUser); // положили в таблицу обновлённые данные
            log.info("Данные пользователя {} успешно обновлены", user);
            return "Данные пользователя " + user.getName() + " успешно обновлены";
        } else {
            log.warn("Введён неверный id");
            throw new UserNotFoundException("пользователя с ID " + user.getId() + " нет");
        }

    }

    // метод для получения списка всех пользователей
    public Map<Integer, User> getAllUsers() {
        return users;
    }

    // метод удаления пользователя
    public String removeUser(User user) {
        return null;
    }
}
