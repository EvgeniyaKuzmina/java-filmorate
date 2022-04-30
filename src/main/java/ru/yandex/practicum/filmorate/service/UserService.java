package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ValidationUser validationUser;
    private final InMemoryUserStorage inMemoryUserStorage;

    // метод для добавления в друзья
    public String addFriend(Integer userId, Integer friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователя c id %d нет", userId));
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Пользователя c id %d нет", friendId));
        }
        User user = inMemoryUserStorage.getUsers().get(userId);
        User friend = inMemoryUserStorage.getUsers().get(friendId);
        user.setFriends(friend);
        friend.setFriends(user);
        return String.format("Пользователь %s и пользователь %s теперь друзья", user.getName(), friend.getUserId());

    }

    // удаление из друзей
    public String removeFriend(Integer userId, Integer friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователя c id %d нет", userId));
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Пользователя c id %d нет", friendId));
        }
        User user = inMemoryUserStorage.getUsers().get(userId);
        User friend = inMemoryUserStorage.getUsers().get(friendId);
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        return String.format("Пользователь %s удалён из друзей", friend.getUserId());

    }

    // вывод списка общих друзей
    public List<User> getCommonFriends(Integer userId, Integer otherId) throws UserNotFoundException {
        validationUser.checkUserById(userId);
        validationUser.checkUserById(otherId);
        Set<User> userFriends = inMemoryUserStorage.getUsers().get(userId).getFriends();
        Set<User> otherUserFriends = inMemoryUserStorage.getUsers().get(otherId).getFriends();
        return userFriends.stream()
                          .filter(otherUserFriends::contains)
                          .collect(Collectors.toList());

    }


}
