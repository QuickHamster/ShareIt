package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.model.LastBooking;
import ru.practicum.shareit.booking.model.NextBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class ItemCommentsOutputDto {
    @EqualsAndHashCode.Exclude
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name too long!")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description too long!")
    private String description;

    @NotNull
    private Boolean available;

    //private User owner;
    private String authorName;

    private NextBooking nextBooking; // дата и время ближайшего следующего бронирования

    private LastBooking lastBooking; // дата и время последнего бронирования

    //private List<Comment> comments;
    private List<CommentOutputDto> comments;

}

