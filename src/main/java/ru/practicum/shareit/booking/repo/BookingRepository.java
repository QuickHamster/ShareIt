package ru.practicum.shareit.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long>  {

    @Query(" select b from Booking b " +
            "where b.bookerId = ?1" +
            " and b.status = ?2")
    List<Booking> getBookings(long userId, BookingState state);

    @Query(" select b from Booking b " +
            "where b.bookerId = ?1" +
            " and b.status = ?2" )
    List<Booking> getItemBookings(long userId, BookingState state);
}
