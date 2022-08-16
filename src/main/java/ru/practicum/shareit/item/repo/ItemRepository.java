package ru.practicum.shareit.item.repo;


import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getAll();
    void deleteAll();
    Item addItem(Item item);
    Item changeItem(Item item);
    Item finItemById(long id);
    long deleteItem(long id);
}
