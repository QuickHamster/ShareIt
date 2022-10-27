package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор бронирования

    @Column(name = "start_date")
    private LocalDateTime start; // дата и время начала бронирования

    @Column(name = "end_time")
    private LocalDateTime end; // дата и время конца бронирования

    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item item; // вещь, которую пользователь бронирует

    @ManyToOne()
    @JoinColumn(name = "booker_id")
    private User booker; // пользователь, который осуществляет бронирование

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // статус бронирования
}
