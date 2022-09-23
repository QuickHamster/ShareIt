package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class BookingInputDto {
    @FutureOrPresent(message = "Date cannot be in the past.")
    private LocalDateTime start; // дата и время начала бронирования

    @FutureOrPresent(message = "Date cannot be in the past.")
    private LocalDateTime end; // дата и время конца бронирования

    private Long itemId; // вещь, которую пользователь бронирует
}
