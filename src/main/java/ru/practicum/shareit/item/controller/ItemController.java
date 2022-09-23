package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;
    private static final String X_HEADER = "X-Sharer-User-Id";

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemOutputDto> getAllItems(@RequestHeader(X_HEADER) long userId) {
        log.info("Получение списка вещей пользователя id = {}.", userId);
        return itemService.getAllItems(userId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_HEADER) long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Добавляется вещь {}.", itemDto.getName());
        return itemService.addItem(userId, itemDto);
    }

    @PutMapping
    public ItemDto changeItem(@RequestHeader(X_HEADER) long userId,
                              @Valid @RequestBody ItemDto itemDto
    ) {
        log.info("Изменение вещи {}.", itemDto.getName());
        return itemService.changeItem(itemDto, itemDto.getId(), userId);
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

    //POST /items/{itemId}/comment
    @PostMapping("/{itemId}/comment")
    public CommentOutputDto addCommentToItem(@PathVariable long itemId,
                                             @Valid @RequestBody CommentInputDto commentInputDto,
                                             @RequestHeader(X_HEADER) long userId) {
        log.info("Пользователем id = {} добавляется комметарий: {}.", userId, commentInputDto.getText());
        return itemService.addCommentToItem(userId, itemId, commentInputDto);
    }
}
