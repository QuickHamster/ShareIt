package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
public class CommentOutputDto {

    @EqualsAndHashCode.Exclude
    private Long id; // уникальный идентификатор комментария

    private String text; // содержимое комментария

    private Item item; // вещь, к которой относится комментарий

    private String authorName; // автор комментария

    private LocalDate created; // дата создания комментария
}
