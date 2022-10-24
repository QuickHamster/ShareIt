package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

//@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Getter
@Setter
public class BookingOutputDto {
    private Long id;

    //@FutureOrPresent(message = "Date cannot be in the past.")
    private LocalDateTime start; // дата и время начала бронирования

    //@FutureOrPresent(message = "Date cannot be in the past.")
    private LocalDateTime end; // дата и время конца бронирования

    //@NotNull
    private Item item; // вещь, которую пользователь бронирует

    //@NotNull
    private User booker; // пользователь, который осуществляет бронирование

    //@Enumerated(EnumType.STRING)
    private BookingStatus status; // статус бронирования

    /*@Override
    public String toString() {
        return "BookingOutputDto{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", item=" + item +
                ", booker=" + booker +
                ", status=" + status +
                '}';
    }*/

}
