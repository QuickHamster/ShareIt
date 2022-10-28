package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Getter
@Setter
public class BookingOutputDto {
    private Long id;
    private LocalDateTime start; // дата и время начала бронирования
    private LocalDateTime end; // дата и время конца бронирования
    private Item item; // вещь, которую пользователь бронирует
    private User booker; // пользователь, который осуществляет бронирование
    private BookingStatus status; // статус бронирования
}
