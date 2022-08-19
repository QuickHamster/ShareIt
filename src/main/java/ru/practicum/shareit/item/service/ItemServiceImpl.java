package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Item> getAllItems(long userId) {
        return itemRepository.getAll(userId);
    }

    @Override
    public void deleteAll() {
        itemRepository.deleteAll();
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        userRepository.getAll().stream()
                .filter(p -> p.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не существует.", userId)));
        itemDto.setOwner(userRepository.findUserById(userId));
        return ItemMapper.toItemDto(itemRepository.addItem(ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto changeItem(ItemDto itemDto, long id, long userId) {

        userRepository.getAll().stream()
                .filter(p -> p.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь # %d не найден.", userId)));

        Item item = itemRepository.findItemById(id);

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Вещь не принадлежит пользователю # %d .", userId));
        }

        if (StringUtils.hasLength(itemDto.getName())) {
            item.setName(itemDto.getName());
        }

        if (StringUtils.hasLength(itemDto.getDescription())) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null && !itemDto.getAvailable().equals(item.getAvailable())) {
            item.setAvailable(itemDto.getAvailable());
        }

        item = itemRepository.changeItem(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto findItemById(long id) {
        return ItemMapper.toItemDto(itemRepository.findItemById(id));
    }

    @Override
    public long deleteItem(long id) {
        return itemRepository.deleteItem(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (!text.isBlank()) {
            return itemRepository.searchItems(text);
        } else return new ArrayList<>();
    }
}
