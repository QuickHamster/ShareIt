package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemOutputDto> getAllItems(long userId);

    void deleteAll();

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto changeItem(ItemDto itemDto, long id, long userId);

    ItemCommentsOutputDto findItemById(long userId, long id);

    long deleteItem(long id);

    List<Item> searchItems(String text);

    CommentOutputDto addCommentToItem(long userId, long itemId, CommentInputDto commentInputDto);
}
