package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * // TODO . Будет реализовано в последующих спринтах
 */
@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
public class Booking {
    private Long id; // уникальный идентификатор бронирования
    private LocalDateTime start; // дата и время начала бронирования
    private LocalDateTime end; // дата и время конца бронирования
    private ru.practicum.shareit.item.model.Item item; // вещь, которую пользователь бронирует
    private User booker; // пользователь, который осуществляет бронирование
    private Long status; // статус бронирования
}
