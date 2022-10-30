package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class ItemRequestDto {
    @EqualsAndHashCode.Exclude
    private Long id; // уникальный идентификатор запроса
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1024, message = "Description too long!")
    private String description; // текст запроса, содержащий описание требуемой вещи
    private Long requestorId; // пользователь, создавший запрос
    private LocalDateTime created = LocalDateTime.now(); // дата и время создания запроса
}
