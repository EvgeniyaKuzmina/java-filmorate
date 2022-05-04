package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ValidationUser validationUser;
    private final InMemoryUserStorage inMemoryUserStorage;

    // метод для добавления в друзья
    public String addFriend(Integer userId, Integer friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, userId));
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, friendId));
        }
        User user = inMemoryUserStorage.getUsers().get(userId);
        User friend = inMemoryUserStorage.getUsers().get(friendId);
        user.addFriends(friend);
        friend.addFriends(user);
        return String.format("Пользователь %s и пользователь %s теперь друзья", user.getName(), friend.getName());

    }

    // удаление из друзей
    public String removeFriend(Integer userId, Integer friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, userId));
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, friendId));
        }
        User user = inMemoryUserStorage.getUsers().get(userId);
        User friend = inMemoryUserStorage.getUsers().get(friendId);
        if (user.getFriends().containsKey(friendId) && user.getFriends().containsKey(userId)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            return String.format("Пользователь %s удалён из друзей", friend.getId());
        }
        return String.format("Пользователь c ID %d не является другом пользователя id %d", user.getId(),
                             friend.getId());

    }

    // вывод списка общих друзей
    public List<String> getCommonFriends(Integer userId, Integer otherUserId) throws UserNotFoundException {
        validationUser.checkUserById(userId);
        validationUser.checkUserById(otherUserId);
        List<String> userFriends = new ArrayList<>(inMemoryUserStorage.getUsers().get(userId)
                                                                      .getFriends().values());
        List<String> otherUserFriends = new ArrayList<>(inMemoryUserStorage.getUsers().get(otherUserId)
                                                                           .getFriends().values());
        return userFriends.stream()
                          .filter(otherUserFriends::contains)
                          .collect(Collectors.toList());
    }

    // получение списка друзей пользователя по id
    public List<String> getUserFriendById(Integer id) {
        validationUser.checkUserById(id);
        return new ArrayList<>(inMemoryUserStorage.getUsers().get(id).getFriends().values());
    }


}
