package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Component
public class UserMapper {
    //из entity в dto
    public static UserDto toUserDto(Optional<User> user) {
        return new UserDto(
                user.get().getId(),
                user.get().getName(),
                user.get().getEmail()
        );
    }

    //из dto в entity
    public static User toUser(UserDto userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail()
        );
    }
}
