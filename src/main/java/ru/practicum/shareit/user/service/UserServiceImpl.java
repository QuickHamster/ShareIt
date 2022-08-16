package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userStorage) {
        this.userRepository = userStorage;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (userRepository.getAll().stream().anyMatch(p -> p.getEmail().equals(userDto.getEmail()))) {
            throw new ValidationException(String.format("Пользователь с email %s уже существует.",
                    userDto.getEmail()));
        }
        return UserMapper.toUserDto(userRepository.addUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto changeUser(UserDto userDto, Long id) {
        User user = userRepository.findUserById(id);

        if (StringUtils.hasLength(userDto.getName())) {
            user.setName(userDto.getName());
        }

        if (StringUtils.hasLength(userDto.getEmail())) {
            userRepository.getAll().forEach(existUser -> {
                if (existUser.getEmail().equals(userDto.getEmail()))
                    throw new ValidationException(String.format("Пользователь с email %s уже существует.",
                            userDto.getEmail()));
            });
            user.setEmail(userDto.getEmail());
        }

        user = userRepository.changeUser(user);

        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto findUserById(long id) {
        return UserMapper.toUserDto(userRepository.findUserById(id));
    }

    @Override
    public long deleteUser(long id) {
        return userRepository.deleteUser(id);
    }
}
