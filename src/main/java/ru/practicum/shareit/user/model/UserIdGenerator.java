package ru.practicum.shareit.user.model;

public class UserIdGenerator {
    private static long userId;

    public static long createUserId() {
        return ++userId;
    }
}

