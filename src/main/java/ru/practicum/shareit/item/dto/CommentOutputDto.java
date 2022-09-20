package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class CommentOutputDto {

    @EqualsAndHashCode.Exclude
    private Long id; // уникальный идентификатор комментария

    private String text; // содержимое комментария

    private Item item; // вещь, к которой относится комментарий

    //private User author; // автор комментария
    private String authorName;

    private LocalDate created; // дата создания комментария
}
