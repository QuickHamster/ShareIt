package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * // TODO .
 */

@Data
public class ItemDto {
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long request;


    public ItemDto(String name, String description, Boolean available, Long request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}
