package ru.yandex.practicum.filmorate.util;

public class Sequence {
    private static long id = 1;

    public static long getNextId() {
        return id++;
    }
}
