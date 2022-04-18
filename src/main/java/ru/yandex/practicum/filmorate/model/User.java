package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class User {

    private Integer userId;
    private String email;
    private String login;
    private String userName;
    private LocalDate birthDay;

    public User(String email, String login, String userName, LocalDate birthDay) {
        this.email = email;
        this.login = login;
        this.userName = userName;
        this.birthDay = birthDay;
    }
}
