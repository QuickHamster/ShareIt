package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void deleteAllUsers();
    UserDto addUser(UserDto userDto);
    UserDto changeUser(UserDto userDto, Long id);
    UserDto findUserById(long id);
    long deleteUser(long id);
}
