package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemCommentsOutputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemMapperTest {

    private ItemMapper itemMapper;
    private Item item;
    private Comment comment;
    private CommentOutputDto commentOutputDto;
    private ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        User user = new User(1L, "user", "user@yandex.ru");
        item = new Item(1L, "itemName", "itemDescription", true, user, 2L);
        commentOutputDto = new CommentOutputDto(1L,
                "textComment", item, "authorName", LocalDate.now());
        itemDto = new ItemDto(1L, "itemName", "itemDescription", true, user, 2L);
        comment = new Comment(1L, "textComment", item, user, LocalDate.now());
    }

    @Test
    void toItemDto() {
        ItemDto fromItemMapper = ItemMapper.toItemDto(item);
        assertNotNull(fromItemMapper);
        assertEquals(item.getId(), fromItemMapper.getId());
        assertEquals(item.getName(), fromItemMapper.getName());
        assertEquals(item.getDescription(), fromItemMapper.getDescription());
        assertEquals(item.isAvailable(), fromItemMapper.getAvailable());
        assertEquals(item.getOwner(), fromItemMapper.getOwner());
        assertEquals(item.getRequestId(), fromItemMapper.getRequestId());
    }

    @Test
    void toItemOutputDto() {
        ItemOutputDto fromItemMapper = ItemMapper.toItemOutputDto(item);
        assertNotNull(fromItemMapper);
        assertEquals(item.getId(), fromItemMapper.getId());
        assertEquals(item.getName(), fromItemMapper.getName());
        assertEquals(item.getDescription(), fromItemMapper.getDescription());
        assertEquals(item.isAvailable(), fromItemMapper.getAvailable());
        assertEquals(item.getOwner(), fromItemMapper.getOwner());
        assertNull(fromItemMapper.getNextBooking());
        assertNull(fromItemMapper.getLastBooking());
    }

    @Test
    void toItemCommentsOutputDto() {
        List<CommentOutputDto> listOutputDto = List.of(commentOutputDto);
        ItemCommentsOutputDto fromItemMapper = ItemMapper.toItemCommentsOutputDto(item, listOutputDto);
        assertNotNull(fromItemMapper);
        assertEquals(item.getId(), fromItemMapper.getId());
        assertEquals(item.getName(), fromItemMapper.getName());
        assertEquals(item.getDescription(), fromItemMapper.getDescription());
        assertEquals(item.isAvailable(), fromItemMapper.getAvailable());
        assertEquals(item.getOwner().getName(), fromItemMapper.getAuthorName());
        assertNull(fromItemMapper.getNextBooking());
        assertNull(fromItemMapper.getLastBooking());
        assertEquals(listOutputDto, fromItemMapper.getComments());
    }

    @Test
    void toItem() {
        Item fromItemMapper = ItemMapper.toItem(itemDto);
        assertNotNull(fromItemMapper);
        assertEquals(itemDto.getId(), fromItemMapper.getId());
        assertEquals(itemDto.getName(), fromItemMapper.getName());
        assertEquals(itemDto.getDescription(), fromItemMapper.getDescription());
        assertEquals(itemDto.getAvailable(), fromItemMapper.getAvailable());
        assertEquals(itemDto.getOwner(), fromItemMapper.getOwner());
        assertEquals(itemDto.getRequestId(), fromItemMapper.getRequestId());
    }

    @Test
    void toCommentOutputDto() {
        CommentOutputDto fromItemMapper = ItemMapper.toCommentOutputDto(comment);
        assertNotNull(fromItemMapper);
        assertEquals(comment.getId(), fromItemMapper.getId());
        assertEquals(comment.getText(), fromItemMapper.getText());
        assertEquals(comment.getItem(), fromItemMapper.getItem());
        assertEquals(comment.getAuthor().getName(), fromItemMapper.getAuthorName());
        assertEquals(comment.getCreated(), fromItemMapper.getCreated());
    }

    @Test
    void toListItemDto() {
        Collection<Item> items = List.of(item);
        List<ItemDto> fromItemMapper = ItemMapper.toListItemDto(items);
        assertNotNull(fromItemMapper);
        assertEquals(items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList()), fromItemMapper);
    }
}
