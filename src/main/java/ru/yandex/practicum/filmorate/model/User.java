package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {
    private Set<Long> friends; // хранит друзей в формате id
    private Long id;
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


/*   public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }*/
    /*public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }*/

    public void addFriends(User friend) {
        friends.add(friend.getId());
    }


    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}
