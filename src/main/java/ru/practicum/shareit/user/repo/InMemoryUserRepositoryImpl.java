/*
package ru.practicum.shareit.user.repo;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryUserRepositoryImpl{// implements UserRepository {

   */
/* private static final AtomicLong ID_GENERATOR = new AtomicLong();

    private final Map<Long, User> users = new LinkedHashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public User addUser(User user) {
        user.setId(ID_GENERATOR.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User changeUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else throw new NotFoundException(String.format("Пользователь # %d не найден.", user.getId()));
    }

    @Override
    public User findUserById(long id) {
        return users.values().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь # %d не найден.", id)));
    }

    @Override
    public long deleteUser(long id) {
        if (users.containsKey(id)) {
            users.remove(id);
            return id;
        } else throw new NotFoundException(String.format("Пользователь # %d не найден.", id));
    }*//*

}
*/
