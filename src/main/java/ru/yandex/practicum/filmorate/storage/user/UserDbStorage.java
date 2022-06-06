package ru.yandex.practicum.filmorate.storage.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.Constants;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage inMemoryUserStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate, InMemoryUserStorage inMemoryUserStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.inMemoryUserStorage = inMemoryUserStorage;

    }

    //создаёт пользователя и возвращает ответ пользователю
    @Override
    public User createUser(User user) {
        long id = saveAndReturnId(user); // добавлили пользователя в таблицу и получили id
        //inMemoryUserStorage.setUsers(id, user);
        return User.builder()
                .id(id)
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    //добавляет пользователя в таблицу и возвращает Id
    private long saveAndReturnId(User user) {
        String sql = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    // обновляет пользователя
    @Override
    public User updateUser(User user) throws ValidationException {
        String sql = "update users SET email = ?, login = ?, name = ?, birthday = ? where id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("Select * from users where id = ?", user.getId());
        if (userRows.next()) {
            inMemoryUserStorage.setUsers(user.getId(), user);
            jdbcTemplate.update(sql,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info("Данные пользователя {} успешно обновлены", user.getName());
            return user;
        } else {
            log.warn("Введён неверный id");
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, user.getId()));
        }
    }

    // получает список всех пользователей
    @Override
    public List<User> getAllUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> UserFromDB.makeUser(rs, jdbcTemplate));

    }

    // удаляет пользователя по id
    @Override
    public String removeUser(Long id) {
        String sql = "delete from users where id = ?";
        if (jdbcTemplate.update(sql, id) == 0) {
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, id));
        }
        return String.format("Пользователь с id %d удалён", id);
    }


    //получает пользователя по id
    @Override
    public User getUsersById(Long id) {
        String sql = "select * from users where id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getString("id"), userRows.getString("login"));
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> UserFromDB.makeUser(rs, jdbcTemplate), id);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException(String.format(Constants.USER_NOT_EXIST, id));
        }
    }


    // Добавляет пользователя в друзья (или подписчики)
    @Override
    public String addFriend(Long userId, Long friendId) {
        String sql = "insert into friends (user_id, friend_id) values (?, ?)";
        String sqlLFriends = "select friend_id from friends where user_id = ?";
        User user = getUsersById(userId);
        User friend = getUsersById(friendId);
        user.setFriends(new HashSet<>(jdbcTemplate.query(sqlLFriends,
                (rs, rowNum) -> UserFromDB.friendId(rs), userId)));
        friend.setFriends(new HashSet<>(jdbcTemplate.query("select friend_id from friends where user_id = ?",
                (rs, rowNum) -> UserFromDB.friendId(rs), friendId)));
        if (user.getFriends().contains(friendId) && friend.getFriends().contains(userId)) {// проверяем
            // если каждый пользователь есть в списке друзей у другого пользователя
            return String.format(
                    "Пользователь %s — %s и пользователь %s — %s уже друзья",
                    userId, user.getName(), friendId, friend.getName());
        } else if (user.getFriends().contains(friendId)) {
            return String.format(
                    "Пользователь %s — %s уже подписан на пользователя %s — %s.",
                    userId, user.getName(), friendId, friend.getName());
        } else if (!user.getFriends().contains(friendId) && friend.getFriends().contains(userId)) {
            jdbcTemplate.update(sql,
                    userId,
                    friendId);
            return String.format("Пользователь %s — %s и пользователь %s — %s теперь друзья",
                    userId, user.getName(), friendId, friend.getName());
        } else {
            jdbcTemplate.update(sql,
                    userId,
                    friendId);
            return String.format(
                    "Пользователь %s — %s подписался на пользователя %s — %s.",
                    userId, user.getName(), friendId, friend.getName());
        }
    }


    // удаление из друзей
    @Override
    public String removeFriend(Long userId, Long friendId) {
        String sql = "delete from friends where user_id = ? and friend_id = ?";
        User user = getUsersById(userId);
        User friend = getUsersById(friendId);
        if (jdbcTemplate.update(sql, userId, friendId) == 0) {
            return String.format("Пользователь %d — %s не является другом пользователя %d — %s",
                    userId, user.getName(), friendId, friend.getName());
        }
        return String.format("Пользователь %d удалён из друзей", friendId);
    }

    // вывод списка общих друзей
    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) throws UserNotFoundException {
        User user = getUsersById(userId);
        User otherUser = getUsersById(otherUserId);
        user.setFriends(new HashSet<>(jdbcTemplate.query("select friend_id from friends where user_id = ?",
                (rs, rowNum) -> UserFromDB.friendId(rs), userId))); //получаем список друзей пользователя
        otherUser.setFriends(new HashSet<>(jdbcTemplate.query("select friend_id from friends where user_id = ?",
                (rs, rowNum) -> UserFromDB.friendId(rs), otherUserId))); //получаем список друзей другого пользователя
        List<Long> commonFriendsId = new ArrayList<>();
        user.getFriends().forEach(id -> {
            if (otherUser.getFriends().contains(id)) commonFriendsId.add(id); // находим общих друзей,
            // добавляем в  отдельный список
        });
        List<User> commonFriends = new ArrayList<>();
        commonFriendsId.forEach(id -> commonFriends.add(getUsersById(id))); // получаем юзеров по id
        return commonFriends;
    }

    // получение списка друзей пользователя по id
    @Override
    public List<User> getUserFriendById(Long id) {
        User user = getUsersById(id);
        user.setFriends(new HashSet<>(jdbcTemplate.query("select friend_id from friends where user_id = ?",
                (rs, rowNum) -> UserFromDB.friendId(rs), id))); //получаем список друзей пользователя
        List<User> userFriends = new ArrayList<>();
        user.getFriends().forEach(friendId -> userFriends.add(getUsersById(friendId)));
        return userFriends;
    }

    @Override
    public Map<Long, User> getUsers() {
        return null;
    }

    @Override
    public void setUsers(long id, User newUser) {
        return;
    }
}
