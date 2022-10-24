package ru.practicum.shareit.booking.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(" select b from Booking b " +
            "where b.booker.id = ?1 group by b.id order by b.start desc")
    List<Booking> getAllBookings(long userId, Pageable pageable);

    @Query(value = " select b from Booking b " +
            "where b.booker.id = ?1" +
            " and b.start > ?2 and b.start > ?2 group by b.id order by b.start desc")
    List<Booking> getBookingsByStateFuture(long userId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = " select b from Booking b " +
            "where b.booker.id = ?1" +
            " and b.start < ?2 and b.end > ?2 group by b.id order by b.start desc")
    List<Booking> getBookingsByStateCurrent(long userId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = " select b from Booking b " +
            "where b.booker.id = ?1" +
            " and b.end < ?2 group by b.id order by b.start desc")
    List<Booking> getBookingsByStatePast(long userId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = " select b from Booking b " +
            "where b.booker.id = ?1" +
            " and b.status = ?2")
        //  group by b.id order by b.start desc
    List<Booking> getBookingsByState(long userId, BookingStatus bookingStatus, Pageable pageable);

    @Query(" select b from Booking b left join Item as i on b.item.id = i.id " +
            "where i.owner.id = ?1 group by b.id order by b.start desc")
    List<Booking> getBookingsForAllItemsUser(long userId, Pageable pageable);

    @Query(" select b from Booking b left join Item as i on b.item.id = i.id " +
            "where i.owner.id = ?1 and b.start > ?2 group by b.id order by b.start desc")
    List<Booking> getBookingsForAllItemsUserByStateFuture(long userId, LocalDateTime localDateTime, Pageable pageable);

    @Query(" select b from Booking b left join Item as i on b.item.id = i.id " +
            "where i.owner.id = ?1 and b.start < ?2 and b.end > ?2 group by b.id order by b.start desc")
    List<Booking> getBookingsForAllItemsUserByStateCurrent(long userId, LocalDateTime localDateTime, Pageable pageable);

    @Query(" select b from Booking b left join Item as i on b.item.id = i.id " +
            "where i.owner.id = ?1 and b.end < ?2 group by b.id order by b.start desc")
    List<Booking> getBookingsForAllItemsUserByStatePast(long userId, LocalDateTime localDateTime, Pageable pageable);

    @Query(" select b from Booking b left join Item as i on b.item.id = i.id " +
            "where i.owner.id = ?1 and b.status = ?2")
    List<Booking> getBookingsForAllItemsUserByState(long userId, BookingStatus bookingStatus, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.booker.id = ?1 and b.end < ?2 ")
    List<Booking> getBookingByBookerAndEndIsBefore(long bookerId, LocalDateTime end);

    Optional<Booking> findFirstByItemOrderByEndDesc(Item item);

    Optional<Booking> findFirstByItemOrderByStartAsc(Item item);
}
