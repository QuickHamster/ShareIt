package ru.practicum.shareit.item.repo;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class InMemoryItemRepositoryImpl {// implements ItemRepository {

  /*  private static final AtomicLong ID_GENERATOR = new AtomicLong();

    private final Map<Long, Item> items = new LinkedHashMap<>();

    @Override
    public List<Item> getAll(long id) {
        return items.values().stream().filter(p -> p.getOwner().getId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        items.clear();
    }

    @Override
    public Item addItem(Item item) {
        item.setId(ID_GENERATOR.incrementAndGet());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item changeItem(Item item) {
        if (items.containsKey(item.getId())) {
            items.put(item.getId(), item);
            return item;
        } else throw new NotFoundException(String.format("Вещь с id # %d не найдена.", item.getId()));
    }

    @Override
    public Item findItemById(long id) {
        return items.values().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id # %d не найдена.", id)));
    }

    @Override
    public long deleteItem(long id) {
        if (items.containsKey(id)) {
            items.remove(id);
            return id;
        } else throw new NotFoundException(String.format("Вещь с id # %d не найдена.", id));
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values().stream()
                .filter(p -> (p.getName().toLowerCase().contains(text.toLowerCase()) || p.getDescription().toLowerCase()
                        .contains(text.toLowerCase())) && p.isAvailable())
                .collect(Collectors.toList());
    }*/
}
