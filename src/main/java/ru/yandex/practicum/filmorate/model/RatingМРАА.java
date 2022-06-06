package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exceptions.RatingNotFoundException;

public enum RatingМРАА {
    G, // у фильма нет возрастных ограничений
    PG, //детям рекомендуется смотреть фильм с родителями
    PG13, //детям до 13 лет просмотр не желателен
    R, // лицам до 17 лет просматривать фильм можно только в присутствии взрослого
    NC17; //лицам до 18 лет просмотр запрещён.

    public static RatingМРАА gerRatingMPAA(String rating) {
        switch (rating) {
            case "G":
                return G;
            case "PG":
                return PG;
            case "PG13":
                return PG13;
            case "R":
                return R;
            default:
                return NC17;
        }
    }

    static public int getRatingId(Film film) {
        switch (Integer.parseInt(film.getMpa().get("id").toString())) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                throw new RatingNotFoundException("Рейтинг фильма может быть только:\n" +
                        "G — у фильма нет возрастных ограничений\n" +
                        "PG — детям рекомендуется смотреть фильм с родителями\n" +
                        "PG13 — детям до 13 лет просмотр не желателен\n" +
                        "R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого\n" +
                        "NC17 — лицам до 18 лет просмотр запрещён.");
        }

    }
}
