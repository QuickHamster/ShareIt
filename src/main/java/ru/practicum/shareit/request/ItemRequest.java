package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemRequest {
    private Long Id; // уникальный идентификатор запроса
    private String description; // текст запроса, содержащий описание требуемой вещи
    private User requestor; // пользователь, создавший запрос
    private LocalDateTime created; // дата и время создания запроса
}
