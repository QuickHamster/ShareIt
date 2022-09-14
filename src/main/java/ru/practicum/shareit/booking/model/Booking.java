package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
/*@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter*/
//@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
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

  /*  @OneToMany()
    @JoinColumn(name = "item_id")*/
    //@Column(name = "item_id")
  @ManyToOne()
  @JoinColumn(name = "item_id")
    private Item item; // вещь, которую пользователь бронирует

   /* @OneToMany()
    @JoinColumn(name = "booker_id")*/
    //@Column(name = "booker_id")
   @ManyToOne()
   @JoinColumn(name = "booker_id")
    private User booker; // пользователь, который осуществляет бронирование

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // статус бронирования
}
