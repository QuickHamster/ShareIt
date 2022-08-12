package ru.practicum.shareit.user.repo;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserIdGenerator;

import java.util.*;

@Component
public class InMemoryUserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new LinkedHashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void save(User user) {
        user.setId(UserIdGenerator.createUserId());
        users.put(user.getId(), user);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }
}
