package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.IncorrectStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              ItemRepository itemRepository) {
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

    // Добавление бронирования. Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи.
    @Override
    public BookingOutputDto add(long userId, BookingInputDto bookingInputDto) {
        if (bookingInputDto.getEnd().isBefore(bookingInputDto.getStart())) {
            throw new UnavailableException("Время окончания не может быть раньше начала бронирования.");
        }
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);

        Optional<User> user = userRepository.findById(userId);
        validationUser(user, userId);
        booking.setBooker(user.get());

        Optional<Item> item = itemRepository.findById(bookingInputDto.getItemId());
        validationItem(item, bookingInputDto.getItemId());

        if (!item.get().isAvailable()) {
            throw new UnavailableException(String.format("Вещь с id %x недоступна для бронирования.",
                    item.get().getId()));
        }
        booking.setItem(item.get());
        if (booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Вещь не принадлежит пользователю id = %x.", userId));
        }
        booking.setEnd(bookingInputDto.getEnd());
        booking.setStart(bookingInputDto.getStart());
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingOutputDto(booking);
    }

    @Override
    public long delete(long id) {
        bookingRepository.deleteById(id);
        return id;
    }

    // Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
    @Override
    public BookingOutputDto approvedBooking(long userId, long bookingId, boolean approved) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        validationBooking(booking, bookingId);
        if (booking.get().getStatus().equals(BookingStatus.APPROVED)) {
            throw new UnavailableException(String.format("Бронирование с id = %x уже подтверждено.", bookingId));
        }
        if (!booking.get().getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Пользователь с id = %x не является владельцем.", userId));
        }
        if (approved) {
            booking.get().setStatus(BookingStatus.APPROVED);
        } else {
            booking.get().setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingOutputDto(bookingRepository.save(booking.get()));
    }

    //Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования,
    // либо владельцем вещи, к которой относится бронирование
    @Override
    public BookingOutputDto findBooking(long userId, long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        validationBooking(booking, bookingId);

        long itemId = booking.get().getItem().getId();
        Optional<Item> item = itemRepository.findById(itemId);
        validationItem(item, itemId);

        if (booking.get().getBooker().getId().equals(userId) || item.get().getOwner().getId().equals(userId)) {

            return BookingMapper.toBookingOutputDto(booking.get());
        } else
            throw new NotFoundException(String.format("Пользователь с id = %x не является автором/владельцем вещи.",
                    userId));
    }

    // Получение списка всех бронирований текущего пользователя
    @Override
    public List<Booking> getUserBookings(long userId, BookingState state, int from, int size) {
        Optional<User> user = userRepository.findById(userId);
        validationUser(user, userId);
        validationPageable(from, size);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());
        switch (state) {
            case ALL:
                return bookingRepository.getAllBookings(userId, pageable);
            case FUTURE:
                return bookingRepository.getBookingsByStateFuture(userId, LocalDateTime.now(), pageable);
            case CURRENT:
                return bookingRepository.getBookingsByStateCurrent(userId, LocalDateTime.now(), pageable);
            case PAST:
                return bookingRepository.getBookingsByStatePast(userId, LocalDateTime.now(), pageable);
            case REJECTED:
                return bookingRepository.getBookingsByState(userId, BookingStatus.REJECTED, pageable);
            case WAITING:
                return bookingRepository.getBookingsByState(userId, BookingStatus.WAITING, pageable);
            default:
                throw new IncorrectStatusException(String.format("Unknown state: %s", state));
        }
    }

    // Получение списка бронирований для всех вещей текущего пользователя
    @Override
    public List<Booking> getBookingsForAllItemsUser(long userId, BookingState state, int from, int size) {
        List<Item> allById = itemRepository.findAllById(userId);
        if (allById.isEmpty()) {
            throw new NotFoundException(String.format("У пользователя с id = %x не существует вещей.", userId));
        }
        Optional<User> user = userRepository.findById(userId);
        validationUser(user, userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());
        switch (state) {
            case ALL:
                return bookingRepository
                        .getBookingsForAllItemsUser(userId, pageable);
            case FUTURE:
                return bookingRepository
                        .getBookingsForAllItemsUserByStateFuture(userId, LocalDateTime.now(), pageable);
            case CURRENT:
                return bookingRepository
                        .getBookingsForAllItemsUserByStateCurrent(userId, LocalDateTime.now(), pageable);
            case PAST:
                return bookingRepository
                        .getBookingsForAllItemsUserByStatePast(userId, LocalDateTime.now(), pageable);
            case REJECTED:
                return bookingRepository
                        .getBookingsForAllItemsUserByState(userId, BookingStatus.REJECTED, pageable);
            case WAITING:
                return bookingRepository
                        .getBookingsForAllItemsUserByState(userId, BookingStatus.WAITING, pageable);
            default:
                throw new IncorrectStatusException(String.format("Unknown state: %s", state));
        }
    }

    private void validationUser(Optional<User> user, long userId) {
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %x не существует.", userId));
        }
    }

    private void validationItem(Optional<Item> item, long itemId) {
        if (item.isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id = %x не существует.", itemId));
        }
    }

    private void validationBooking(Optional<Booking> booking, long bookingId) {
        if (booking.isEmpty()) {
            throw new NotFoundException(String.format("Бронирование с id = %x не существует.", bookingId));
        }
    }

    private void validationPageable(int from, int size) {
        if (from < 0) {
            throw new UnavailableException(String
                    .format("Стартовый элемент выборки %d не может быть меньше 0.", from));
        }
        if (size < 1) {
            throw new UnavailableException(String
                    .format("Количество элементов выборки %d не может быть меньше 1.", size));
        }
    }
}
