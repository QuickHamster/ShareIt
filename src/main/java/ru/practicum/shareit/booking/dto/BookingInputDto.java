package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Getter
@Setter
public class BookingInputDto {
    @FutureOrPresent(message = "Date cannot be in the past.")
    private LocalDateTime start; // дата и время начала бронирования

    @FutureOrPresent(message = "Date cannot be in the past.")
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
