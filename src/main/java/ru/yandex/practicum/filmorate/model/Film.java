package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Set<User> likes; // хранит пользователей, кто поставил лайк фильму
    private final Duration duration;
    @NotBlank
    @NotNull
    private final String name;
    private Integer id;
    private LocalDate releaseDate;
    @Length(max = 200)
    private String description;

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
    }

    public void setLike(User user) {
        likes.add(user);
    }
}
