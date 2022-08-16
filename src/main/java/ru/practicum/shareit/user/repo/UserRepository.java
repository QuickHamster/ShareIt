package ru.practicum.shareit.user.repo;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();
    void deleteAll();
    User addUser(User user);
    User changeUser(User user);
    User findUserById(long id);
    long deleteUser(long id);
}
