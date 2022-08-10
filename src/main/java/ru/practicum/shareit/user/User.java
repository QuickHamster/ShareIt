package ru.practicum.shareit.user;

import lombok.Data;

/**
 * // TODO .
 */
@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
public class User {
    private Long Id; // уникальный идентификатор пользователя
    private String name; // имя или логин пользователя
    private String email; // адрес электронной почты (уникальный)
}
