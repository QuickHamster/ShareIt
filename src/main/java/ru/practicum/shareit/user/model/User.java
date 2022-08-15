package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * // TODO .
 */
@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class User {

    @EqualsAndHashCode.Exclude
    private Long Id; // уникальный идентификатор пользователя

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name too long!")
    private String name; // имя или логин пользователя

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email has to be correct")
    private String email; // адрес электронной почты (уникальный)

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("email", email);
        return values;
    }
}
