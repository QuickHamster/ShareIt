package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class ItemServiceImplTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;

    private ItemDto itemDto;
    private ItemDto itemDto1;
    private ItemDto itemDtos;
    private ItemDto itemDtos1;
    private UserDto userDto;
    private UserDto userDto1;
    private UserDto userDtos;
    private UserDto userDtos1;
    private CommentInputDto commentInputDto;


    @BeforeEach
    void beforeEach_1() {

        userDto = new UserDto(0L, "user", "user@yandex.ru");
        userDtos = userService.addUser(userDto);

        userDto1 = new UserDto(0L, "user1", "user1@yandex.ru");
        userDtos1 = userService.addUser(userDto1);

        itemDto = new ItemDto(0L, "itemName", "itemDescription", true, UserMapper.toUser(userDtos), null);
        itemDto1 = new ItemDto(0L, "itemName1", "itemDescription1", true, UserMapper.toUser(userDtos1), null);
        itemDtos = itemService.addItem(userDtos.getId(), itemDto);
        itemDtos1 = itemService.addItem(userDtos1.getId(), itemDto1);

        commentInputDto = new CommentInputDto("textComment");
    }

    @Test
    void addCommentToItem() {
        // создание бронирования первого предмета
        BookingInputDto bookingInputDto = new BookingInputDto(LocalDateTime.now(), LocalDateTime.now().plusNanos(5), itemDtos.getId());
        // добавление бронирования в БД: второй пользователь бронирует вещь первого пользователя
        BookingOutputDto bookingOutputDto = bookingService.add(userDtos1.getId(), bookingInputDto);
        // подтверждение бронирования: первый пользователь подтвеждает бронь первого предмета
        bookingService.approvedBooking(userDtos.getId(), bookingOutputDto.getId(), true);
        // добавление комментария: второй пользователь добавляет комментарий к первому предмету
        CommentOutputDto commentOutputDto = itemService.addCommentToItem(userDtos1.getId(), itemDtos.getId(), commentInputDto);
        assertNotNull(commentOutputDto);
        assertEquals(commentOutputDto.getText(), commentInputDto.getText());
    }

    @AfterEach
    void afterEach() {
        bookingService.deleteAll();
        itemService.deleteAll();
        userService.deleteAllUsers();
    }

    @Test
    void addItem() {
        assertNotNull(itemDtos);
        assertEquals(itemDto, itemDtos);
        assertEquals(itemDto.getName(), itemDtos.getName());
    }

    @Test
    void changeItem() {
        itemDtos = itemService.changeItem(itemDto1, itemDtos.getId(), userDtos.getId());
        assertNotNull(itemDtos);
        assertEquals(itemDto1.getName(), itemDtos.getName());
        assertEquals(itemDto1.getDescription(), itemDtos.getDescription());
    }

    @Test
    void findItemById() {
        ItemCommentsOutputDto itemCommentsOutputDto = itemService.findItemById(userDtos.getId(), itemDtos.getId());
        assertNotNull(itemCommentsOutputDto);
        assertEquals(itemCommentsOutputDto.getId(), itemDtos.getId());
        assertEquals(itemCommentsOutputDto.getName(), itemDtos.getName());
        assertEquals(itemCommentsOutputDto.getDescription(), itemDtos.getDescription());
        assertEquals(itemCommentsOutputDto.getAvailable(), itemDtos.getAvailable());
        assertEquals(itemCommentsOutputDto.getAuthorName(), itemDtos.getOwner().getName());
    }

    @Test
    void findItemByIdTestBooking() {
        ItemCommentsOutputDto itemCommentsOutputDto = itemService.findItemById(userDtos.getId(), itemDtos.getId());
        assertNotNull(itemCommentsOutputDto);
        assertEquals(itemCommentsOutputDto.getId(), itemDtos.getId());
        assertEquals(itemCommentsOutputDto.getName(), itemDtos.getName());
        assertEquals(itemCommentsOutputDto.getDescription(), itemDtos.getDescription());
        assertEquals(itemCommentsOutputDto.getAvailable(), itemDtos.getAvailable());
        assertEquals(itemCommentsOutputDto.getAuthorName(), itemDtos.getOwner().getName());
    }

    @Test
    void deleteItem() {
        itemService.deleteItem(itemDtos.getId());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> itemService.findItemById(userDto.getId(), itemDtos.getId()));
        assertEquals(String.format("Вещь с id = %x не существует.", itemDtos.getId()), ex.getMessage());
    }

    @Test
    void searchItems() {
        List<Item> items = itemService.searchItems("iTem");
        assertNotNull(items);
        assertTrue(items.size() > 0);
    }

    @Test
    void getAllItems() {
        List<ItemOutputDto> listItemOutputDto = itemService.getAllItems(userDtos.getId());
        assertNotNull(listItemOutputDto);
    }

}
