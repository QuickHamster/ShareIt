package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public class ItemMapper {

    public static ItemDto toItemDto(Optional<Item> item) {
        return new ItemDto(
                item.get().getId(),
                item.get().getName(),
                item.get().getDescription(),
                item.get().isAvailable(),
                item.get().getOwner()/*,
                item.get().getRequest() != null ? item.get().getRequest() : null*/
        );
    }

    public static Item toItem(ItemDto itemDTO) {
        return new Item(
                itemDTO.getId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                itemDTO.getOwner()/*,
                itemDTO.getRequest()*/
        );
    }

}
