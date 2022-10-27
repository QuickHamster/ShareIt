package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserMapperTest {
    private UserMapper userMapper;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto(7L, "user1", "user1@yandex.ru");
        user = new User(0L, "user2", "user2@yandex.ru");
    }

    @Test
    void toUser() {
        User userFromDto = UserMapper.toUser(userDto);
        userFromDto.setId(7L);
        assertNotNull(userFromDto);
        assertEquals(userDto.getId(), userFromDto.getId());
        assertEquals(userDto.getName(), userFromDto.getName());
        assertEquals(userDto.getEmail(), userFromDto.getEmail());
    }

    @Test
    void toUserDto() {
        UserDto userDtoFromMapper = UserMapper.toUserDto(user);
        assertNotNull(userDtoFromMapper);
        assertEquals(user.getId(), userDtoFromMapper.getId());
        assertEquals(user.getName(), userDtoFromMapper.getName());
        assertEquals(user.getEmail(), userDtoFromMapper.getEmail());
    }
}
