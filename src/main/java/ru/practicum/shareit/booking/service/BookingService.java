package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    List<Booking> getAll();

    void deleteAll();

    BookingDto add(long userId, BookingDto bookingDto);

    BookingDto change(BookingDto bookingDto, Long id);

    BookingDto findById(long id);

    long delete(long id);

    BookingDto approvedBooking(long userId, long bookingId, boolean approved);

    BookingDto findBooking(long userId, long bookingId);

    List<Booking> getBookings(long userId, BookingState state);

    List<Booking> getItemBookings(long userId, BookingState state);
}
