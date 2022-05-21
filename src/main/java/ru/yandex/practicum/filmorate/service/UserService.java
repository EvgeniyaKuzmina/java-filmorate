package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ValidationUser validationUser;
    private final UserStorage userStorage;

    // метод для добавления в друзья
    public String addFriend(Long userId, Long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, userId));
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, friendId));
        }
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        user.addFriends(friend);
        friend.addFriends(user);
        return String.format("Пользователь %s и пользователь %s теперь друзья", user.getName(), friend.getName());

    }

    // удаление из друзей
    public String removeFriend(Long userId, Long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, userId));
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, friendId));
        }
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        if (user.getFriends().contains(friendId) && user.getFriends().contains(userId)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            return String.format("Пользователь %s удалён из друзей", friend.getId());
        }
        return String.format("Пользователь c ID %d не является другом пользователя id %d", user.getId(),
                             friend.getId());

    }

    // вывод списка общих друзей
    public List<User> getCommonFriends(Long userId, Long otherUserId) throws UserNotFoundException {
        validationUser.checkUserById(userId);
        validationUser.checkUserById(otherUserId);
        Set<Long> userFriends = new HashSet<>(userStorage.getUsers().get(userId)
                                                            .getFriends());
        Set<Long> otherUserFriends = new HashSet<>(userStorage.getUsers().get(otherUserId)
                                                                 .getFriends());
        List<Long> commonIdFriend = userFriends.stream()
                                                  .filter(otherUserFriends::contains)
                                                  .collect(Collectors.toList());
        List<User> commonFriend = new ArrayList<>();
        for (Long id : commonIdFriend) {
            commonFriend.add(userStorage.getUsers().get(id));
        }
        return commonFriend;
    }

    // получение списка друзей пользователя по id
    public List<User> getUserFriendById(Long id) {
        validationUser.checkUserById(id);
        List<Long> userFriendsId = new ArrayList<>(userStorage.getUsers().get(id).getFriends());
        List<User> userFriends = new ArrayList<>();
        for (Long idUser : userFriendsId) {
            userFriends.add(userStorage.getUsers().get(idUser));
        }
        return userFriends;
    }


}
