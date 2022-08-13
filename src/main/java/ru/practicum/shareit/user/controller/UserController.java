package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.exception.model.User;

import javax.validation.Valid;

/**
 * // TODO .
 */

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return null;
    }
}
