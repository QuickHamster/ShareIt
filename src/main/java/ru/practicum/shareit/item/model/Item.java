package ru.practicum.shareit.item.model;


import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.exception.model.User;

/**
 * // TODO .
 */

@Data // Аннотация  добавит геттеры и сеттеры, а также методы toString(), equals(User other) и hashCode()
public class Item {
    private Long Id; // уникальный идентификатор вещи
    private String name; // краткое название
    private String description; // развёрнутое описание
    private Boolean available; // статус о том, доступна или нет вещь для аренды
    private User owner; // владелец вещи
    private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос

    public Item(String name, String description, Boolean available, User owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        //this.request = request;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
