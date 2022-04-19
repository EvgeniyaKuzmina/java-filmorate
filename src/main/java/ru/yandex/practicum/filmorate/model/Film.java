package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private final Duration duration;
    @NotBlank
    @NotNull
    private final String filmName;
    private Integer filmId;
    private LocalDate releaseDate;
    @Length(max = 200)
    private String description;

    public Film(String filmName, String description, LocalDate releaseDate, Duration duration) {
        this.filmName = filmName;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

}
