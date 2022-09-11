package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Booking> getAll() {

        return bookingRepository.findAll();
    }

    @Override
    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    @Override
    //@Transactional
    public BookingDto add(long userId, BookingDto bookingDto) {
        //bookingDto.setBookerId(userId);
        bookingDto.setStatus(BookingStatus.WAITING);
        //return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingDto)));
        Booking booking = BookingMapper.toBooking(bookingDto,userId, bookingDto.getItemId(), bookingDto.getStatus());
        booking = bookingRepository.save(booking);
        BookingDto bookingDto1 = BookingMapper.toBookingDto(booking);
        return bookingDto1;
    }

    @Override
    public BookingDto change(BookingDto bookingDto, Long id) {
        return null;
    }

    @Override
    public BookingDto findById(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирование с id = %x не существует.", id));
        }
        return BookingMapper.toBookingDto(booking.get());
    }

    @Override
    public long delete(long id) {
        bookingRepository.deleteById(id);
        return id;
    }

    @Override
    @Transactional
    public BookingDto approvedBooking(long userId, long bookingId, boolean approved) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирование с id = %x не существует.", bookingId));
        }
        if (!booking.get().getBookerId().equals(userId))  {
            throw new ValidationException(String.format("Пользователь с id = %x не является владельцем.", userId));
        }
        if (approved) {
            booking.get().setStatus(BookingStatus.APPROVED);
        }
        else {
            booking.get().setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDto(booking.get());
    }

    //Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования,
    // либо владельцем вещи, к которой относится бронирование
    @Override
    public BookingDto findBooking(long userId, long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирование с id = %x не существует.", bookingId));
        }

        long itemId = booking.get().getItemId();
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id = %x не существует.", itemId));
        }

        if (booking.get().getBookerId().equals(userId) || item.get().getOwner().getId().equals(userId))  {
            return BookingMapper.toBookingDto(booking.get());
        } else throw new ValidationException(String.format("Пользователь с id = %x не является автором/владельцем вещи.", userId));
    }

    // Получение списка всех бронирований текущего пользователя
    @Override
    public List<Booking> getBookings(long userId, BookingState state) {
        return bookingRepository.getBookings(userId, state);
    }

    @Override
    public List<Booking> getItemBookings(long userId, BookingState state) {
        return bookingRepository.getItemBookings(userId, state);
    }
}
