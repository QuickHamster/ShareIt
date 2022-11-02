package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestListItemDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestInputDto);

    List<ItemRequestListItemDto> getAllRequests(long userId, int from, int size);

    List<ItemRequestListItemDto> getItemRequestsByUser(long userId);

    ItemRequestListItemDto getItemRequestByRequestId(long userId, long requestId);
}
