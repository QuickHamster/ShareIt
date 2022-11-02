package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    private static final String X_HEADER = "X-Sharer-User-Id";

    // Добавление бронирования. Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи.
    @PostMapping
    public BookingOutputDto add(@RequestHeader(X_HEADER) long userId,
                                @RequestBody BookingInputDto bookingInputDto) {
        log.info("Добавляется бронирование.");
        BookingOutputDto bookingOutputDto = bookingService.add(userId, bookingInputDto);
        log.info("Бронирование добавлено.");
        return bookingOutputDto;
    }

    // Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
    //PATCH /bookings/{bookingId}?approved={approved}
    @PatchMapping(path = "/{bookingId}")
    public BookingOutputDto approvedBooking(@RequestHeader(X_HEADER) long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam boolean approved) {
        log.info("Подтверждение/отклонение бронирование: {}", approved);
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    // Получение данных о конкретном бронировании (включая его статус).
    // Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование
    // GET /bookings/{bookingId}
    @GetMapping(path = "/{bookingId}")
    public BookingOutputDto findBooking(@RequestHeader(X_HEADER) long userId,
                                        @PathVariable long bookingId) {
        log.info("Поиск бронирования: {}", bookingId);
        return bookingService.findBooking(userId, bookingId);
    }

    //Получение списка всех бронирований текущего пользователя
    // Бронирования должны возвращаться отсортированными по дате от более новых к более старым
    //GET /bookings?state={state}
    @GetMapping
    public List<Booking> getUserBookings(@RequestHeader(X_HEADER) long userId,
                                         @RequestParam(defaultValue = "ALL") BookingState state,
                                         @RequestParam(value = "from",
                                                 required = false,
                                                 defaultValue = "0")  int from,
                                         @RequestParam(value = "size",
                                                 required = false,
                                                 defaultValue = "15")  int size) {
        log.info("Получение списка бронирований для пользователя id = {} начиная с " +
                "элемента {} в количестве {}", userId, from, size);
        return bookingService.getUserBookings(userId, state, from, size);
    }

    // Получение списка бронирований для всех вещей текущего пользователя.
    // Бронирования должны возвращаться отсортированными по дате от более новых к более старым
    // GET /bookings/owner?state={state}
    @GetMapping(path = "/owner")
    public List<Booking> getBookingsForAllItemsUser(@RequestHeader(X_HEADER) long userId,
                                                    @RequestParam(defaultValue = "ALL") BookingState state,
                                                    @RequestParam(name = "from",
                                                            defaultValue = "0") Integer from,
                                                    @RequestParam(name = "size",
                                                            defaultValue = "20") Integer size) {
        log.info("Получение списка бронирований для всех вещей текущего пользователя id = {} " +
                "начиная с элемента {} в количестве {}", userId, from, size);
        return bookingService.getBookingsForAllItemsUser(userId, state, from, size);
    }
}
