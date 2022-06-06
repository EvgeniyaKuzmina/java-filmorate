package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.util.DurationSerialize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {


    @JsonSerialize(using = DurationSerialize.class)
    private Duration duration;
    @NotBlank
    @NotNull
    private String name;
    private Set<Long> likes; // хранит id пользователей, кто поставил лайк фильму
    private Long id;
    private LocalDate releaseDate;
    @Length(max = 200)
    @NotBlank
    @NotNull
    private String description;
    private Genre genre;
    private RatingМРАА rating;

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
    }

    public void setLike(User user) {
        likes.add(user.getId());
    }
}
