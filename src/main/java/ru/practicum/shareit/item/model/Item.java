package ru.practicum.shareit.item.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля класса
public class Item {
    private Long id; // уникальный идентификатор вещи
    private String name; // краткое название
    private String description; // развёрнутое описание
    private Boolean available; // статус о том, доступна или нет вещь для аренды
    private User owner; // владелец вещи
    private ItemRequest request; // если вещь была создана по запросу другого пользователя,
                                 // то в этом поле будет храниться ссылка на соответствующий запрос

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
