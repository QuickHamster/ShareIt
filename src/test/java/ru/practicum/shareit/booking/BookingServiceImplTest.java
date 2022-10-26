package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IncorrectStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    private Item item;
    private ItemDto itemDto;
    private User booker;
    private User owner;
    private User thirdUser;
    private BookingInputDto bookingInputDto;
    private BookingOutputDto bookingOutputDto;

    private final LocalDateTime startDT = LocalDateTime.now().plusDays(1);

    private final LocalDateTime endDT = LocalDateTime.now().plusDays(5);

    @BeforeEach
    void beforeEach_1() {
        owner = new User(0L, "owner", "owner@yandex.ru");
        booker = new User(0L, "booker", "booker@yandex.ru");
        thirdUser = new User(0L, "third", "third@yandex.ru");
        owner = UserMapper.toUser(userService.addUser(UserMapper.toUserDto(owner)));
        booker = UserMapper.toUser(userService.addUser(UserMapper.toUserDto(booker)));
        thirdUser = UserMapper.toUser(userService.addUser(UserMapper.toUserDto(thirdUser)));

        item = new Item(0L, "item", "itemDescription", true, owner, null);
        item = ItemMapper.toItem(itemService.addItem(owner.getId(), ItemMapper.toItemDto(item)));

        bookingInputDto = new BookingInputDto(startDT, endDT, item.getId());
        bookingOutputDto = bookingService.add(booker.getId(), bookingInputDto);
    }

    @Test
    void addBooking() {
        assertThat(bookingOutputDto.getId()).isNotZero();
        assertThat(bookingOutputDto.getBooker().getId()).isEqualTo(booker.getId());
        assertThat(bookingOutputDto.getBooker().getName()).isEqualTo(booker.getName());
        assertThat(bookingOutputDto.getStart()).isEqualTo(startDT);
        assertThat(bookingOutputDto.getEnd()).isEqualTo(endDT);
        assertThat(bookingOutputDto.getItem().getId()).isEqualTo(item.getId());
        assertThat(bookingOutputDto.getItem().getName()).isEqualTo(item.getName());
        assertThat(bookingOutputDto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void addBookingIncorrectDate() {
        bookingInputDto = new BookingInputDto(endDT, startDT, item.getId());
        Throwable throwable = assertThrows(
                UnavailableException.class,
                () -> bookingService.add(booker.getId(), bookingInputDto)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void approvedBooking() {
        bookingOutputDto.setStatus(BookingStatus.APPROVED);
        BookingOutputDto approvedBooking = bookingService.approvedBooking(owner.getId(), bookingOutputDto.getId(), true);
        assertNotNull(approvedBooking);
        assertEquals(bookingOutputDto.getId(), approvedBooking.getId());
        assertEquals(bookingOutputDto.getBooker(), approvedBooking.getBooker());
        assertEquals(bookingOutputDto.getItem(), approvedBooking.getItem());
        assertEquals(bookingOutputDto.getStatus(), approvedBooking.getStatus());
    }

    @Test
    void deleteBooking() {
        bookingService.delete(bookingOutputDto.getId());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> bookingService.findBooking(owner.getId(), item.getId()));
        assertEquals(String.format("Бронирование с id = %x не существует.", item.getId()), ex.getMessage());
    }

    @Test
    void findBooking() {
        BookingOutputDto findBooking = bookingService.findBooking(owner.getId(), bookingOutputDto.getId());
        assertNotNull(findBooking);
        assertEquals(bookingOutputDto.getId(), findBooking.getId());
        assertEquals(bookingOutputDto.getItem(), findBooking.getItem());
        assertEquals(bookingOutputDto.getBooker(), findBooking.getBooker());
    }

    @Test
    void getUserBookingsALL() {
        List<Booking> bookingList = bookingService.getUserBookings(owner.getId(), BookingState.ALL, 0, 1);
        assertNotNull(bookingList);
    }

    @Test
    void getUserBookingsUNSUPPORTED_STATUS() {
        Throwable throwable = assertThrows(
                IncorrectStatusException.class,
                () -> bookingService.getUserBookings(owner.getId(), BookingState.UNSUPPORTED_STATUS, 0, 1)
        );
        assertNotNull(throwable.getMessage());
    }



    @Test
    void getUserBookingsFUTURE() {
        List<Booking> bookingList = bookingService.getUserBookings(owner.getId(), BookingState.FUTURE, 0, 1);
        assertNotNull(bookingList);
    }

    @Test
    void getUserBookingsCURRENT() {
        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        bookingInputDto = new BookingInputDto(start, end, item.getId());
        bookingOutputDto = bookingService.add(booker.getId(), bookingInputDto);
        List<Booking> bookingList = bookingService.getUserBookings(owner.getId(), BookingState.CURRENT, 0, 2);
        assertNotNull(bookingList);
    }

    @Test
    void getUserBookingsPAST() {
        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now().minusDays(1);
        bookingInputDto = new BookingInputDto(start, end, item.getId());
        bookingOutputDto = bookingService.add(booker.getId(), bookingInputDto);
        List<Booking> bookingList = bookingService.getUserBookings(owner.getId(), BookingState.PAST, 0, 2);
        assertNotNull(bookingList);
    }

    @Test
    void getUserBookingsREJECTED() {
        BookingOutputDto approvedBooking = bookingService.approvedBooking(owner.getId(), bookingOutputDto.getId(), false);
        List<Booking> bookingList = bookingService.getUserBookings(owner.getId(), BookingState.REJECTED, 0, 2);
        assertNotNull(bookingList);
    }

    @Test
    void getUserBookingsWAITING() {
        List<Booking> bookingList = bookingService.getUserBookings(owner.getId(), BookingState.WAITING, 0, 2);
        assertNotNull(bookingList);
    }

    @Test
    void getBookingsForAllItemsUserALL() {
        List<Booking> bookingList = bookingService.getBookingsForAllItemsUser(owner.getId(), BookingState.ALL, 0, 1);
        assertNotNull(bookingList);
    }

    @Test
    void getBookingsForAllItemsUserFUTURE() {
        List<Booking> bookingList = bookingService.getBookingsForAllItemsUser(owner.getId(), BookingState.FUTURE, 0, 1);
        assertNotNull(bookingList);
    }

    @Test
    void getBookingsForAllItemsUserCURRENT() {
        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        bookingInputDto = new BookingInputDto(start, end, item.getId());
        bookingOutputDto = bookingService.add(booker.getId(), bookingInputDto);
        List<Booking> bookingList = bookingService.getBookingsForAllItemsUser(owner.getId(), BookingState.CURRENT, 0, 2);
        assertNotNull(bookingList);
    }

    @Test
    void getBookingsForAllItemsUserPAST() {
        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now().minusDays(1);
        bookingInputDto = new BookingInputDto(start, end, item.getId());
        bookingOutputDto = bookingService.add(booker.getId(), bookingInputDto);
        List<Booking> bookingList = bookingService.getBookingsForAllItemsUser(owner.getId(), BookingState.PAST, 0, 2);
        assertNotNull(bookingList);
    }

    @Test
    void getBookingsForAllItemsUserREJECTED() {
        BookingOutputDto approvedBooking = bookingService.approvedBooking(owner.getId(), bookingOutputDto.getId(), false);
        List<Booking> bookingList = bookingService.getBookingsForAllItemsUser(owner.getId(), BookingState.REJECTED, 0, 2);
        assertNotNull(bookingList);
    }

    @Test
    void getBookingsForAllItemsUserWAITING() {
        List<Booking> bookingList = bookingService.getBookingsForAllItemsUser(owner.getId(), BookingState.WAITING, 0, 2);
        assertNotNull(bookingList);
    }
}
