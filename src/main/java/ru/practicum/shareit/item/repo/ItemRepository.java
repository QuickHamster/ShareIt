package ru.practicum.shareit.item.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository  extends JpaRepository<Item, Long> {
    /*List<Item> getAll(long id);

    void deleteAll();

    Item addItem(Item item);

    Item changeItem(Item item);

    Item findItemById(long id);

    long deleteItem(long id);*/
    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    List<Item> searchItems(String text);
}
