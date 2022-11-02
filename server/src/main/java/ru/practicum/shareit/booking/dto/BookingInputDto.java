package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Getter
@Setter
public class BookingInputDto {
    private LocalDateTime start; // дата и время начала бронирования
    private LocalDateTime end; // дата и время конца бронирования
    private Long itemId; // вещь, которую пользователь бронирует

    @Override
    public String toString() {
        return "BookingInputDto{" +
                "start=" + start +
                ", end=" + end +
                ", itemId=" + itemId +
                '}';
    }
}
