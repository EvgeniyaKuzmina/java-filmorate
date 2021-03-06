package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Set;

public class Id {

    private static final Long ONE = 1L;

    private Id() {
    }

    //генерирует новый id на основании последнего созданного Id
    public static Long getId(Set<Long> id) {
        Comparator<Long> comparator = Comparator.comparingLong(id2 -> id2);
        LinkedList<Long> allId = new LinkedList<>(id);
        allId.sort(comparator);
        if (allId.isEmpty()) {
            return ONE;
        }
        return allId.getLast() + ONE;

    }
}
