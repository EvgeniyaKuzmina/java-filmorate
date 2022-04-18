package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private final Duration duration;
    private final String filmName;
    private Integer filmId;
    private LocalDate dateOfRelease;
    private String description;

    public Film(String filmName, String description, LocalDate dateOfRelease, Duration duration) {
        this.filmName = filmName;
        this.description = description;
        this.dateOfRelease = dateOfRelease;
        this.duration = duration;
    }

}
