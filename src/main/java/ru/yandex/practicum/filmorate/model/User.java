package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Set<Integer> friends; // хранит друзей в формате id
    private Integer id;
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotBlank
    @NotNull
    private String login;
    @NotNull
    private String name;
    private LocalDate birthday;
    private Boolean status; /* статус дружбы:
                            false неподтверждённая — когда один пользователь отправил запрос
                            на добавление другого пользователя в друзья,
                            true подтверждённая — когда второй пользователь согласился на добавление.
                              */

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }

    public void addFriends(User friend) {
        friends.add(friend.getId());
    }
}
