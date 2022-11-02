package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение списка пользователей.");
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("Добавляется пользователь {}.", userDto.getName());
        return userService.addUser(userDto);
    }

    @PutMapping
    public UserDto changeUser(@RequestBody UserDto userDto) {
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
