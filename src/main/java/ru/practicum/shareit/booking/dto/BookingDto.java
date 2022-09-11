package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
//@NoArgsConstructor
public class BookingDto {
    @EqualsAndHashCode.Exclude
    //private Long id;

    @FutureOrPresent(message="Date cannot be in the past.")
    private LocalDateTime start; // дата и время начала бронирования

    @FutureOrPresent(message="Date cannot be in the past.")
    private LocalDateTime end; // дата и время конца бронирования

    //@NotNull
    private Long itemId; // вещь, которую пользователь бронирует

    //@NotNull
    private Long bookerId; // пользователь, который осуществляет бронирование

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // статус бронирования
}
