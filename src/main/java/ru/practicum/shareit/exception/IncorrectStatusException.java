package ru.practicum.shareit.exception;

public class IncorrectStatusException extends RuntimeException {

    public IncorrectStatusException() {
    }

    public IncorrectStatusException(String s) {
        super(s);
    }
}
