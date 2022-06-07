package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;

public class UserFromDB {
    private final static String SQL_GET_FRIENDS = "select friend_id from friends where user_id = ?";

    public static User makeUser(ResultSet rs, JdbcTemplate jdbcTemplate) throws SQLException {
        Long id = rs.getLong("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();

        user.setFriends(new HashSet<>(jdbcTemplate.query(SQL_GET_FRIENDS, (rsFriends, rowNum) -> friendId(rsFriends), id)));
        return user;
    }

    public static Long friendId(ResultSet rs) throws SQLException {
        return rs.getLong("friend_id");


    }
}
