package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mpa { // хранит информацию о рейтинге фильма

    private Long id;
    private RatingМРАА mpa;
}
