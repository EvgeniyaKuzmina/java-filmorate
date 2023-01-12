package ru.yandex.practicum.filmorate.model;

public enum Genre {
    COMEDY ("комедия"),
    DRAMA ("драма"),
    CARTOON("мультфильм"),
    THRILLER ("триллер"),
    DOCUMENTARY ("документальный"),
    ACTION ("боевик"),
    MELODRAMA ("мелодрама"),
    HISTORICAL ("исторический"),
    HORROR ("ужасы"),
    FAMILY ("семейный"),
    FANTASTIC ("фантастика"),
    FANTASY ("фэнтези");

    private String genre;

    Genre(String genre) {
        this.genre = genre;
    }
}
