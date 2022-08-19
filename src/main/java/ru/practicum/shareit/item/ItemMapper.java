package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static Item toItem(ItemDto itemDTO) {
        return new Item(
                itemDTO.getId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                itemDTO.getOwner(),
                itemDTO.getRequest()
        );
    }

}
