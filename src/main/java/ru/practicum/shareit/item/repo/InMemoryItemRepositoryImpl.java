package ru.practicum.shareit.item.repo;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryItemRepositoryImpl implements ItemRepository{

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    private final Map<Long, Item> items = new LinkedHashMap<>();

    @Override
    public List<Item> getAll() {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Item addItem(Item item) {
        item.setId(ID_GENERATOR.incrementAndGet());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item changeItem(Item item) {
        return null;
    }

    @Override
    public Item finItemById(long id) {
        return null;
    }

    @Override
    public long deleteItem(long id) {
        return 0;
    }
}
