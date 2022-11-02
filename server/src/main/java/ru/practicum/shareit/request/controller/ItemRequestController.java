package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestListItemDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    // +POST /requests — добавить новый запрос вещи
    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(X_HEADER) long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Добавляется запрос вещи: {}.", itemRequestDto.getDescription());
        return itemRequestService.addItemRequest(userId, itemRequestDto);
    }

    // +GET /requests — получить список своих запросов вместе с данными об ответах на них
    @GetMapping

    public List<ItemRequestListItemDto> getItemRequests(@RequestHeader(X_HEADER) long userId) {
        log.info("Получить список запросов пользователя id = {}.", userId);
        return itemRequestService.getItemRequestsByUser(userId);
    }

    // +GET /requests/{requestId} — получить данные об одном конкретном запросе
    @GetMapping("/{requestId}")
    public ItemRequestListItemDto getItemRequestsById(@RequestHeader(X_HEADER) long userId,
                                                      @PathVariable long requestId) {
        log.info("Получение данных о запросе id = {}.", requestId);
        return itemRequestService.getItemRequestByRequestId(userId, requestId);
    }

    // GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями
    @GetMapping("/all")
    public List<ItemRequestListItemDto> getAllItemRequests(@RequestHeader(X_HEADER) long userId,
                                                           @RequestParam(value = "from",
                                                                   required = false,
                                                                   defaultValue = "0") int from,
                                                           @RequestParam(value = "size",
                                                                   required = false,
                                                                   defaultValue = "15") int size) {
        log.info("Получение запросов пользователя {} начиная с элемента {} в количестве {}", userId, from, size);
        return itemRequestService.getAllRequests(userId, from, size);
    }
}
