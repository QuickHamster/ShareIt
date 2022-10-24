package ru.practicum.shareit.item.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор вещи
    private String name; // краткое название
    private String description; // развёрнутое описание
    @Column(name = "is_available")
    private Boolean available; // статус о том, доступна или нет вещь для аренды
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner; // владелец вещи
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    @ToString.Exclude*/
    private Long requestId; // id запроса, в ответ на который создаётся нужная вещь

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
