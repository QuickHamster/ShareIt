package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class NextBooking {
    private Long id; // уникальный идентификатор бронирования
    private Long bookerId; // пользователь, который осуществляет бронирование
}
