package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserMapper;
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
    public UserController(UserServiceImpl userService, UserMapper userMapper) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение списка пользователей.");
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody User user) {
        log.info("Добавляется пользователь {}.", user.getName());
        return userService.addUser(user);
    }

    @PutMapping
    public UserDto changeUser(@Valid @RequestBody User user) {
        log.info("Изменение пользователя {}.", user.getName());
        return userService.changeUser(UserMapper.toUserDto(user), user.getId());
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
        return userService.changeUser(userDto, id);
    }

}
