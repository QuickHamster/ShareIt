package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    private static final String X_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(X_HEADER) @Positive long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Добавляется вещь {}, userId = {}.", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remove(@Positive @PathVariable Long id) {
        log.info("Удаление вещи id = {}.", id);
        return itemClient.deleteItem(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@Positive @RequestHeader(X_HEADER) long userId,
                                          @Positive @PathVariable long id) {
        log.info("Получение вещи id = {}, userId = {}.", id, userId);
        return itemClient.findItemById(userId, id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchResource(
            @Positive @PathVariable long id,
            @RequestBody ItemDto itemDto,
            @Positive @RequestHeader(X_HEADER) long userId) {
        log.info("Изменение вещи id = {}, itemDto = {}, userId = {}.", id, itemDto, userId);
        return itemClient.changeItem(itemDto, id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@NotBlank @RequestParam String text) {
        log.info("Поиск вещи по вхождению строки: {}.", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@Positive @PathVariable long itemId,
                                                   @Valid @RequestBody CommentInputDto commentInputDto,
                                                   @Positive @RequestHeader(X_HEADER) long userId) {
        log.info("Пользователем id = {} добавляется комметарий: {}, itemId = {}.", userId, commentInputDto.getText(), itemId);
        return itemClient.addCommentToItem(userId, itemId, commentInputDto);
    }
}
