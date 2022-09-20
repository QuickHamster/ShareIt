package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemOutputDto> getAllItems(long userId);

    void deleteAll();

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto changeItem(ItemDto itemDto, long id, long userId);

    ItemOutputDto findItemById(long userId, long id);

    long deleteItem(long id);

    List<Item> searchItems(String text);

    CommentOutputDto addCommentToItem(long userId, long itemId, CommentInputDto commentInputDto);
}
