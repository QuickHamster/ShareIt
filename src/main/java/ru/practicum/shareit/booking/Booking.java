package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.model.User;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
public class Booking {
    private Long Id; // уникальный идентификатор бронирования
    private LocalDateTime start; // дата и время начала бронирования
    private LocalDateTime end; // дата и время конца бронирования
    private Item item; // вещь, которую пользователь бронирует
    private User booker; // пользователь, который осуществляет бронирование
    private Long status; // статус бронирования
}
