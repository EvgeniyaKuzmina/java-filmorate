package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {

    private Set<User> friends;
    private Integer userId;
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

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void setFriends(User friend) {
        friends.add(friend);
    }
}
