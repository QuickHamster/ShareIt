package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    private static final String X_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@Positive @RequestHeader(X_HEADER) long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Добавляется запрос вещи: {}, userId = {}.", itemRequestDto.getDescription(), userId);
        return requestClient.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@Positive @RequestHeader(X_HEADER) long userId) {
        log.info("Получить список запросов пользователя id = {}.", userId);
        return requestClient.getItemRequestsByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestsById(@Positive @RequestHeader(X_HEADER) long userId,
                                                      @Positive @PathVariable long requestId) {
        log.info("Получение данных о запросе id = {}.", requestId);
        return requestClient.getItemRequestByRequestId(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@Positive @RequestHeader(X_HEADER) long userId,
                                                     @RequestParam(value = "from",
                                                                   required = false,
                                                                   defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(value = "size",
                                                                   required = false,
                                                                   defaultValue = "15") @Positive int size) {
        log.info("Получение запросов пользователя {} начиная с элемента {} в количестве {}", userId, from, size);
        return requestClient.getAllRequests(userId, from, size);
    }
}
