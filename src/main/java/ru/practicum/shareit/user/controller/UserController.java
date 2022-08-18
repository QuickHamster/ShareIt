package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение списка пользователей.");
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавляется пользователь {}.", userDto.getName());
        return userService.addUser(userDto);
    }

    @PutMapping
    public UserDto changeUser(@Valid @RequestBody UserDto userDto) {
        log.info("Изменение пользователя {}.", userDto.getName());
        return userService.changeUser(userDto, userDto.getId());
    }

    @DeleteMapping("/{id}")
    public Long remove(@PathVariable Long id) {
        log.info("Удаление пользователя {}.", id);
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("Получение пользователя {}.", id);
        return userService.findUserById(id);
    }

    @PatchMapping("/{id}")
    public UserDto patchResource(
            @PathVariable long id,
            @RequestBody UserDto userDto) {
        log.info("Изменение пользователя {}.", id);
        return userService.changeUser(userDto, id);
    }

}
