package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> getAll();

    void deleteAll();

    BookingOutputDto add(long userId, BookingInputDto bookingInputDto);

    BookingDto change(BookingDto bookingDto, Long id);

    BookingDto findById(long id);

    long delete(long id);

    BookingOutputDto approvedBooking(long userId, long bookingId, boolean approved);

    BookingOutputDto findBooking(long userId, long bookingId);

    List<Booking> getUserBookings(long userId, BookingState state);

    List<Booking> getItemBookings(long userId, BookingState state);

    // Получение списка бронирований для всех вещей текущего пользователя
    List<Booking> getBookingsForAllItemsUser(long userId, BookingState state);
}
