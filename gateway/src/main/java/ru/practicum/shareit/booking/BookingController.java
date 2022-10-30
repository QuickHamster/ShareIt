package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	private static final String X_HEADER = "X-Sharer-User-Id";

	// Добавление бронирования. Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи.
	@PostMapping
	public ResponseEntity<Object> addBooking(@Positive @RequestHeader(X_HEADER) long userId,
								@Valid @RequestBody BookingInputDto bookingInputDto) {
		log.info("Добавляется бронирование {}, userId = {}.", bookingInputDto, userId);
		return bookingClient.addBooking(userId, bookingInputDto);
	}

	@PatchMapping(path = "/{bookingId}")
	public ResponseEntity<Object> approvedBooking(@Positive @RequestHeader(X_HEADER) long userId,
												  @Positive @PathVariable long bookingId,
												  @RequestParam boolean approved) {
		log.info("Подтверждение/отклонение {} бронирование bookingId = {}, userId = {}.", approved, bookingId, userId);
		return bookingClient.approvedBooking(userId, bookingId, approved);
	}

	@GetMapping(path = "/{bookingId}")
	public ResponseEntity<Object> findBooking(@Positive @RequestHeader(X_HEADER) long userId,
											  @Positive @PathVariable long bookingId) {
		log.info("Поиск бронирования: {}, userId = {}", bookingId, userId);
		return bookingClient.findBooking(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getUserBookings(@RequestHeader(X_HEADER) @Positive long userId,
												  @RequestParam(name = "state", defaultValue = "ALL")
												  BookingState state,
										 		  @RequestParam(name = "from", value = "from",
												 	required = false,
												 	defaultValue = "0") @PositiveOrZero int from,
										 		  @RequestParam(name = "size", value = "size",
												 	required = false,
												 	defaultValue = "15") @Positive int size) {
		log.info("Получение списка бронирований для пользователя id = {} начиная с " +
				"элемента {} в количестве {}", userId, from, size);
		return bookingClient.getUserBookings(userId, state, from, size);
	}

	@GetMapping(path = "/owner")
	public ResponseEntity<Object> getBookingsForAllItemsUser(@RequestHeader(X_HEADER) @Positive long userId,
													@RequestParam(name = "state", defaultValue = "ALL")
													BookingState state,
													@RequestParam(name = "from", value = "from",
															defaultValue = "0") @Min(0) Integer from,
													@RequestParam(name = "size", value = "size",
															defaultValue = "20") @Min(1) Integer size) {
		log.info("Получение списка бронирований для всех вещей текущего пользователя id = {} " +
				"начиная с элемента {} в количестве {}", userId, from, size);
		return bookingClient.getBookingsForAllItemsUser(userId, state, from, size);
	}
}
