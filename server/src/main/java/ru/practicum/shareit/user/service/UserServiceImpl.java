package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto changeUser(UserDto userDto, Long id) {
        Optional<User> user = userRepository.findById(id);
        validationUser(user, id);

        if (StringUtils.hasLength(userDto.getName())) {
            user.get().setName(userDto.getName());
        }

        if (StringUtils.hasLength(userDto.getEmail())) {
            userRepository.findAll().forEach(existUser -> {
                if (existUser.getEmail().equals(userDto.getEmail()))
                    throw new ValidationException(String.format("Пользователь с email %s уже существует.",
                            userDto.getEmail()));
            });
            user.get().setEmail(userDto.getEmail());
        }

        user = Optional.of(userRepository.save(user.get()));

        return UserMapper.toUserDto(user.get());
    }

    @Override
    public UserDto findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        validationUser(user, id);
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public long deleteUser(long id) {
        userRepository.deleteById(id);
        return id;
    }

    private void validationUser(Optional<User> user, long userId) {
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %x не существует.", userId));
        }
    }
}
