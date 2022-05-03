package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private InMemoryUserStorage inMemoryUserStorage;
    private UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    // создание пользователя
    @PostMapping
    public String createUser(@Valid @RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.createUser(user);
    }

    // обновление пользователя
    @PutMapping
    public String updateUser(@Valid @RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.updateUser(user);
    }

    // получение всех пользователей
    @GetMapping
    public Map<Integer, User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    // получение пользователя по id
    //TODO написать метод возвращающий пользователя по Id

    // получаем список друзей общих с другим пользователем.
    @GetMapping(value = {"{id}/friends/common/{otherId}", "/friends/common/{otherId}", "{id}/friends/common/", "/friends/common/"})
    @ResponseBody
    public Map<Integer, String> getCommonFriends(@PathVariable(required = false) String id,
                                                 @PathVariable(required = false, value = "otherId") String otherUserId)
            throws ValidationException {
        if (id == null) {
            throw new ValidationException(Constants.USER_ID_IS_EMPTY);
        }
        if (Integer.parseInt(id) <= 0) {
            throw new ValidationException(Constants.USER_ID_INCORRECT);
        }
        if (otherUserId == null) {
            throw new ValidationException(Constants.USER_ID_IS_EMPTY);
        }
        if (Integer.parseInt(otherUserId) <= 0) {
            throw new ValidationException(Constants.USER_ID_INCORRECT);
        }
        return userService.getCommonFriends(Integer.parseInt(id), Integer.parseInt(otherUserId));
    }

    // удаление из друзей
    @DeleteMapping(value = {"{id}/friends/{friendId}", "/friends/{friendId}", "{id}/friends/", "/friends/"})
    @ResponseBody
    public String deleteFriends(@PathVariable(required = false) String id, @PathVariable(required = false) String friendId) throws
                                                                                                                            ValidationException {
        if (id == null || friendId == null) {
            throw new ValidationException(Constants.USER_ID_IS_EMPTY);
        }
        if (Integer.parseInt(id) <= 0 || Integer.parseInt(friendId) <= 0) {
            throw new ValidationException(Constants.USER_ID_INCORRECT);
        }

        return userService.removeFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    // добавление в друзья
    @PutMapping(value = {"{id}/friends/{friendId}", "/friends/{friendId}", "{id}/friends/", "/friends/"})
    @ResponseBody
    public String addFriend(@PathVariable(required = false) String id, @PathVariable(required = false) String friendId) throws
                                                                                                                        ValidationException {
        if (id == null || friendId == null) {
            throw new ValidationException(Constants.USER_ID_IS_EMPTY);
        }
        if (Integer.parseInt(id) <= 0 || Integer.parseInt(friendId) <= 0) {
            throw new ValidationException(Constants.USER_ID_INCORRECT);
        }
        return userService.addFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    //возвращаем список друзей пользователя
    @GetMapping(value = {"{id}/friends", "/friends"})
    @ResponseBody
    public Map<Integer, String> getUserFriendById(@PathVariable(required = false) String id) throws
                                                                                             ValidationException {
        if (id == null) {
            throw new ValidationException(Constants.USER_ID_IS_EMPTY);
        }
        if (Integer.parseInt(id) <= 0) {
            throw new ValidationException(Constants.USER_ID_INCORRECT);
        }
        return userService.getUserFriendById(Integer.parseInt(id));
    }

}
