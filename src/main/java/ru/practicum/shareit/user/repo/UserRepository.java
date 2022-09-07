package ru.practicum.shareit.user.repo;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    void deleteAll();

    User save(User user);

    //User changeUser(User user);

    Optional<User> findById(long id);

    long deleteById(long id);


}
