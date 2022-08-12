package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * // TODO .
 */
@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
//@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class User {
    private Long Id; // уникальный идентификатор пользователя
    private String name; // имя или логин пользователя
    private String email; // адрес электронной почты (уникальный)

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
