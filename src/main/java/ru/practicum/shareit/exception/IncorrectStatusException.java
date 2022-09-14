package ru.practicum.shareit.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncorrectStatusException extends RuntimeException  {

    public IncorrectStatusException() {
    }
    public IncorrectStatusException(String s) {
        super(s);
    }
}
