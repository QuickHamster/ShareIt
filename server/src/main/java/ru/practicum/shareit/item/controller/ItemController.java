package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemOutputDto> getAllItems(@RequestHeader(X_HEADER) long userId) {
        log.info("Получение списка вещей пользователя id = {}.", userId);
        return itemService.getAllItems(userId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_HEADER) long userId,
                           @RequestBody ItemDto itemDto) {
        log.info("Добавляется вещь {}.", itemDto.getName());
        return itemService.addItem(userId, itemDto);
    }

    @DeleteMapping("/{id}")
    public Long remove(@PathVariable Long id) {
        log.info("Удаление вещи id = {}.", id);
        return itemService.deleteItem(id);
    }

    @GetMapping("/{id}")
    public ItemCommentsOutputDto getItem(@RequestHeader(X_HEADER) long userId,
                                         @PathVariable long id) {
        log.info("Получение вещи id = {}.", id);
        return itemService.findItemById(userId, id);
    }

    @PatchMapping("/{id}")
    public ItemDto patchResource(
            @PathVariable long id,
            @RequestBody ItemDto itemDto,
            @RequestHeader(X_HEADER) long userId) {
        log.info("Изменение вещи id = {}.", id);
        return itemService.changeItem(itemDto, id, userId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        log.info("Поиск вещи по вхождению строки: {}.", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutputDto addCommentToItem(@PathVariable long itemId,
                                             @RequestBody CommentInputDto commentInputDto,
                                             @RequestHeader(X_HEADER) long userId) {
        log.info("Пользователем id = {} добавляется комметарий: {}.", userId, commentInputDto.getText());
        return itemService.addCommentToItem(userId, itemId, commentInputDto);
    }
}
