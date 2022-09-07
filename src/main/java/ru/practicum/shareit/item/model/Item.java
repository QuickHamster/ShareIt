package ru.practicum.shareit.item.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // уникальный идентификатор вещи
    private String name; // краткое название
    private String description; // развёрнутое описание
    @Column(name = "is_available")
    private Boolean available; // статус о том, доступна или нет вещь для аренды
    @Column(name = "owner_id")
    private User owner; // владелец вещи
    @Column(name = "request_id")
    private ItemRequest request; // если вещь была создана по запросу другого пользователя,
                                 // то в этом поле будет храниться ссылка на соответствующий запрос

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
