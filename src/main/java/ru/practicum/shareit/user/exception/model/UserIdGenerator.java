package ru.practicum.shareit.user.exception.model;

public class UserIdGenerator {
    private static long userId;
    public static long createUserId() {
        return ++userId;
    }
}

