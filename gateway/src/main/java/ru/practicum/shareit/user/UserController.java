package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(
            @Valid @RequestBody UserDto userDto) {
        log.info("Добавляется пользователь {}.", userDto.getName());
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchResource(
            @Positive @PathVariable Long id,
            @RequestBody UserDto userDto) {
        log.info("Изменение пользователя {}.", id);
        return userClient.changeUser(userDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remove(
            @Positive @PathVariable Long id) {
        log.info("Удаление пользователя {}.", id);
        return userClient.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(
            @Positive @PathVariable Long id) {
        log.info("Получение пользователя {}.", id);
        return userClient.findUserById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получение списка пользователей.");
        return userClient.getAllUsers();
    }
}
