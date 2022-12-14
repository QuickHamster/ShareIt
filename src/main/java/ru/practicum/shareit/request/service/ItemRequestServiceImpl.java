package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestListItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repo.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  ItemRepository itemRepository,
                                  UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto) {
        User user = validationUser(userId);
        return ItemRequestMapper.toItemRequestDto(
                itemRequestRepository.save(ItemRequestMapper.toItemRequest(user, itemRequestDto)));
    }

    @Override
    public List<ItemRequestListItemDto> getAllRequests(long userId, int from, int size) {
        validationUser(userId);
        validationPageable(from, size);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());
        List<ItemRequest> itemRequests = itemRequestRepository.findOtherRequestor(userId, pageable);
        return itemRequests.stream().map(p -> ItemRequestMapper.toItemRequestListItemDto(p,
                        ItemMapper.toListItemDto(itemRepository.findItemsByRequestId(p.getId()))))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestListItemDto> getItemRequestsByUser(long userId) {
        validationUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserId(userId);
        return itemRequests.stream().map(p -> ItemRequestMapper.toItemRequestListItemDto(p,
                        ItemMapper.toListItemDto(itemRepository.findItemsByRequestId(p.getId()))))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestListItemDto getItemRequestByRequestId(long userId, long requestId) {
        validationUser(userId);
        ItemRequest itemRequest = validationItemRequest(requestId);
        return ItemRequestMapper.toItemRequestListItemDto(itemRequest,
                ItemMapper.toListItemDto(itemRepository.findItemsByRequestId(requestId)));
    }

    private User validationUser(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("???????????????????????? ?? id = %x ???? ????????????????????.", userId));
        }
        return user.get();
    }

    private ItemRequest validationItemRequest(long requestId) {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        if (itemRequest.isEmpty()) {
            throw new NotFoundException(String.format("???????????? ?? id = %x ???? ????????????????????.", requestId));
        }
        return itemRequest.get();
    }

    private void validationPageable(int from, int size) {
        if (from < 0) {
            throw new UnavailableException(String
                    .format("?????????????????? ?????????????? ?????????????? %d ???? ?????????? ???????? ???????????? 0.", from));
        }
        if (size < 1) {
            throw new UnavailableException(String
                    .format("???????????????????? ?????????????????? ?????????????? %d ???? ?????????? ???????? ???????????? 1.", size));
        }
    }
}
