package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemCommentsOutputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner()/*,
                item.get().getRequest() != null ? item.get().getRequest() : null*/
        );
    }

    public static ItemOutputDto toItemOutputDto(Item item) {
        return new ItemOutputDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                null,
                null
        );
    }

    public static ItemCommentsOutputDto toItemCommentsOutputDto(Item item, Comment comment) {
        return new ItemCommentsOutputDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                null,
                null,
                comment.getText()
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

    public static CommentOutputDto toCommentOutputDto(Comment comment) {
        return new CommentOutputDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
