package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Getter
public enum Genre {


    COMEDY ("комедия"),
    DRAMA ("драма"),
    CARTOON ("мультфильм"),
    THRILLER ("трилер"),
    DOCUMENTARY ("документальный"),
    ACTION ("боевик"),
    MELODRAMA ("мелодрама"),
    HISTORICAL ("исторический"),
    HORROR ("ужасы"),
    FAMILY ("семейный"),
    FANTASTIC ("фантастика"),
    FANTASY ("фэнтези");


    private String genreName;
    Genre (String genreName) {
        this.genreName = genreName;
    }

    public static Genre getGenreName(String genreName) {
        switch (genreName) {
            case "комедия":
                return COMEDY;
            case "драма":
                return DRAMA;
            case "мультфильм":
                return CARTOON;
            case "трилер":
                return THRILLER;
            case "документальный":
                return DOCUMENTARY;
            case "боевик":
                return ACTION;
            case "мелодрама":
                return MELODRAMA;
            case "исторический":
                return HISTORICAL;
            case "ужасы":
                return HORROR;
            case "семейный":
                return FAMILY;
            case "фантастика":
                return FANTASTIC;
            default:
                return FANTASY;
        }
    }

    static public List<Integer> getGenreId(Film film) {
        final List<Integer> genreId = new ArrayList<>();
        List<Genre> genres = film.getGenre();
        genres.forEach(g ->log.warn(g.getGenreName()));
        genres.forEach(g ->log.warn(String.valueOf(g)));
        genres.forEach(g -> {
            switch (g.getGenreName()) {
                case "комедия":
                    genreId.add(1);
                    break;
                case "драма":
                    genreId.add(2);
                    break;
                case "мультфильм":
                    genreId.add(3);
                    break;
                case "трилер":
                    genreId.add(4);
                    break;
                case "документальный":
                    genreId.add(5);
                    break;
                case "боевик":
                    genreId.add(6);
                    break;
                case "мелодрама":
                    genreId.add(7);
                    break;
                case "исторический":
                    genreId.add(8);
                    break;
                case "ужасы":
                    genreId.add(9);
                    break;
                case "семейный":
                    genreId.add(10);
                    break;
                case "фантастика":
                    genreId.add(11);
                    break;
                case "фэнтези":
                    genreId.add(12);
                    break;
                default:
                    throw new GenreNotFoundException("Указан неверный жанр фильма.");
            }
        });
        return genreId;
    }

}
