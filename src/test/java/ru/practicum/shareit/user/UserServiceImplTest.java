package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper  userMapper;

    private UserDto userDto1;
    private UserDto userDto2;

    private UserDto userDtos;

    @BeforeEach
    void beforeEach_1() {
        userDto1 = new UserDto(0L, "user1", "user1@yandex.ru");
        userDto2 = new UserDto(0L, "user2", "user2@yandex.ru");
        userDtos = userService.addUser(userDto1);
    }

    @AfterEach
    void AfterEach_1() {
        userService.deleteAllUsers();
    }

    @Test
    void getAllUsers() {
        List<User> userList = List.of(UserMapper.toUser(userDtos));
        assertEquals(userList, userService.getAllUsers());
    }

    @Test
    void addUser() {
        assertNotNull(userDtos);
        assertEquals(userDto1, userDtos);
    }

    @Test
    void changeUser() {
        userDtos = userService.changeUser(userDto2, userDtos.getId());
        assertNotNull(userDtos);
        assertEquals(userDto2, userDtos);
    }

    @Test
    void findUserById() {
        userDtos = userService.findUserById(userDtos.getId());
        assertNotNull(userDtos);
        assertEquals(userDto1, userDtos);
    }

    @Test
    void deleteUser() {
        userService.deleteUser(userDtos.getId());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userService.findUserById(userDtos.getId()));
        assertEquals(String.format("Пользователь с id = %x не существует.", userDtos.getId()), ex.getMessage());
    }

}
