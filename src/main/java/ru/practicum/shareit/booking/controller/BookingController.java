package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    private static final String X_HEADER = "X-Sharer-User-Id";

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader(X_HEADER) long userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        log.info("Добавляется бронирование.");
        BookingDto bookingDto1 = bookingService.add(userId, bookingDto);
        log.info("Бронирование добавлено.");
        return bookingDto1;
    }

    //PATCH /bookings/{bookingId}?approved={approved}
    @PatchMapping(path = "/{bookingId}")
    public BookingDto approvedBooking(@RequestHeader(X_HEADER) long userId,
                                      @PathVariable long bookingId,
                                      @RequestParam boolean approved) {
        log.info("Подтверждение/отклонение бронирование: {}", approved);
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    // GET /bookings/{bookingId}
    @GetMapping(path = "/{bookingId}")
    public BookingDto fingBooking(@RequestHeader(X_HEADER) long userId,
                                      @PathVariable long bookingId) {
        log.info("Поиск бронирование: {}", bookingId);
        return bookingService.findBooking(userId, bookingId);
    }

    //GET /bookings?state={state}
    @GetMapping
    public List<Booking> getBookings(@RequestHeader(X_HEADER) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Получение списка бронирований для пользователя id = {}", userId);
        return (List<Booking>) bookingService.getBookings(userId, state);
    }
}
