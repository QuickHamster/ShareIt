package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemRequestAnswerDto {
    private Long id;

    //@NotBlank(message = "Description cannot be blank")
    //@Size(max = 1024, message = "Description too long!")
    private String description; // текст запроса, содержащий описание требуемой вещи

    //@FutureOrPresent(message = "Date cannot be in the past.")
    private LocalDateTime created; // дата и время создания запроса

    //private Set<ItemDto> items;
}
