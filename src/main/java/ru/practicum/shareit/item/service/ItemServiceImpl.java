package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<Item> getAllItems(long userId) {
        return itemRepository.findAllById(Collections.singleton(userId));
    }

    @Override
    public void deleteAll() {
        itemRepository.deleteAll();
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        userRepository.findAll().stream()
                .filter(p -> p.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не существует.", userId)));
        itemDto.setOwner(userRepository.findById(userId).get());
        return ItemMapper.toItemDto(Optional.of(itemRepository.save(ItemMapper.toItem(itemDto))));
    }

    @Override
    public ItemDto changeItem(ItemDto itemDto, long id, long userId) {

        userRepository.findAll().stream()
                .filter(p -> p.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь # %d не найден.", userId)));

        Optional<Item> item = itemRepository.findById(id);

        if (!item.get().getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Вещь не принадлежит пользователю # %d .", userId));
        }

        if (StringUtils.hasLength(itemDto.getName())) {
            item.get().setName(itemDto.getName());
        }

        if (StringUtils.hasLength(itemDto.getDescription())) {
            item.get().setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null && !itemDto.getAvailable().equals(item.get().getAvailable())) {
            item.get().setAvailable(itemDto.getAvailable());
        }

        item = Optional.of(itemRepository.save(item.get()));

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto findItemById(long id) {
        return ItemMapper.toItemDto(itemRepository.findById(id));
    }

    @Override
    public long deleteItem(long id) {
        itemRepository.deleteById(id);
        return id;
    }

    @Override
    public List<Item> searchItems(String text) {
        if (!text.isBlank()) {
            return itemRepository.searchItems(text);
        } else return new ArrayList<>();
    }
}
