package ru.practicum.shareit.user.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * // TODO .
 */
@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class User {

    @EqualsAndHashCode.Exclude
    private Long Id; // уникальный идентификатор пользователя

    @NotBlank(message = "Name cannot be blank")
    private String name; // имя или логин пользователя

    @Email(message = "Email has to be correct")
    private String email; // адрес электронной почты (уникальный)

   /* public User(Long id, String name, String email) {
        this.name = name;
        this.email = email;
    }*/
}
