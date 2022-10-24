package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestListItemDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {

    @Autowired
    ItemRequestService itemRequestService;
    @Autowired
    UserService userService;
    private UserDto userDto1;
    private UserDto userDtos;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDto itemRequestDtos;

    @BeforeEach
    void beforeEach() {
        userDto1 = new UserDto(0L, "user1", "user1@yandex.ru");
        userDtos = userService.addUser(userDto1);
        itemRequestDto = new ItemRequestDto(0L, "description", UserMapper.toUser(userDtos), LocalDateTime.now());
        itemRequestDtos = itemRequestService.addItemRequest(userDtos.getId(), itemRequestDto);
    }

    @Test
    void addItemRequest() {
        assertNotNull(itemRequestDto);
        assertEquals(itemRequestDtos.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    void getAllRequests() {
        List<ItemRequestListItemDto> fromService = itemRequestService.getAllRequests(userDtos.getId(), 0,1);
        assertNotNull(fromService);
    }

    @Test
    void getItemRequestsByUser() {
        List<ItemRequestListItemDto> fromService = itemRequestService.getItemRequestsByUser(userDtos.getId());
        assertNotNull(fromService);
        assertEquals(fromService.get(0).getDescription(), itemRequestDto.getDescription());
    }

    @Test
    void getItemRequestByRequestId() {
        ItemRequestListItemDto fromService = itemRequestService.getItemRequestByRequestId(userDtos.getId(), itemRequestDtos.getId());
        assertNotNull(fromService);
        assertEquals(itemRequestDtos.getId(), fromService.getId());
        assertEquals(itemRequestDtos.getDescription(), fromService.getDescription());
    }
}
