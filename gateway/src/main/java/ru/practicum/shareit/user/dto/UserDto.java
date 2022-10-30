package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class UserDto {
    @EqualsAndHashCode.Exclude
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name too long!")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email has to be correct")
    private String email;
}
