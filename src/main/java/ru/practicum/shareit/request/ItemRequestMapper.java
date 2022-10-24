package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestListItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequest toItemRequest(User user, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(user);
        itemRequest.setCreated(itemRequestDto.getCreated());
        return itemRequest;
    }

    public static ItemRequestListItemDto toItemRequestListItemDto(ItemRequest itemRequest, List<ItemDto> items) {
        return new ItemRequestListItemDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }

    /*public static ItemRequestInputDto toItemRequestInputDto(ItemRequest itemRequest) {
        return new ItemRequestInputDto(
                itemRequest.getDescription()
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated()
                );
    }

    public static ItemRequestAnswerDto toItemRequestAnswerDto(ItemRequest itemRequest) {
        return new ItemRequestAnswerDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                (itemRequest.getItems() == null) ? null: itemRequest.getItems().stream()
                        .map(ItemMapper::toItemDto).collect(Collectors.toSet())
        );
    }*/
}
