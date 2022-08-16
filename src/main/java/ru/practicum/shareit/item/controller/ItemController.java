package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> getAllItems(@RequestHeader(X_HEADER) long userId) {
        log.info("Получение списка вещей.");
        return itemService.getAllItems(userId);
    }
    @PostMapping
    public ItemDto addItem(@RequestHeader(X_HEADER) long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Добавляется вещь {}.", itemDto.getName());
        return itemService.addItem(userId, itemDto);
    }


}
