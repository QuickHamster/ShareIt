package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.LastBooking;
import ru.practicum.shareit.booking.model.NextBooking;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<ItemOutputDto> getAllItems(long userId) {
        List<Item> itemList = itemRepository.findAllById(userId);
        List<ItemOutputDto> outputDtoList = new ArrayList<>();
        if (!itemList.isEmpty()) {
            for (Item item : itemList) {
                ItemOutputDto itemOutputDto = ItemMapper.toItemOutputDto(item);
                if (item.getOwner().getId().equals(userId)) {
                    Optional<Booking> bookingNext = bookingRepository.findFirstByItemOrderByEndDesc(item);
                    Optional<Booking> bookingLast = bookingRepository.findFirstByItemOrderByStartAsc(item);
                    setBooking(itemOutputDto, bookingNext, bookingLast);
                }
                outputDtoList.add(itemOutputDto);
            }
        }
        return outputDtoList;
    }

    @Override
    public void deleteAll() {
        itemRepository.deleteAll();
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId);
        validationUser(user, userId);
        itemDto.setOwner(user.get());
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto changeItem(ItemDto itemDto, long id, long userId) {
        Optional<User> user = userRepository.findById(userId);
        validationUser(user, userId);

        Optional<Item> item = itemRepository.findById(id);
        validationItem(item, id);

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

        return ItemMapper.toItemDto(item.get());

    }

    @Override
    public ItemCommentsOutputDto findItemById(long userId, long id) {
        Optional<Item> item = itemRepository.findById(id);
        validationItem(item, id);

        Optional<User> user = userRepository.findById(userId);
        validationUser(user, userId);

        List<Comment> comments = commentRepository.findCommentsByItem(id);
        List<CommentOutputDto> commentOutputDto = new ArrayList<>();
        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                commentOutputDto.add(ItemMapper.toCommentOutputDto(comment));
            }
        }
        ItemCommentsOutputDto itemCommentsOutputDto = ItemMapper.toItemCommentsOutputDto(item.get(), commentOutputDto);
        if (item.get().getOwner().getId().equals(userId)) {
            Optional<Booking> bookingNext = bookingRepository.findFirstByItemOrderByEndDesc(item.get());
            Optional<Booking> bookingLast = bookingRepository.findFirstByItemOrderByStartAsc(item.get());
            if (bookingLast.isPresent()) {
                LastBooking lastBooking = new LastBooking(bookingLast.get().getId(),
                        bookingLast.get().getBooker().getId());
                itemCommentsOutputDto.setLastBooking(lastBooking);
            }
            if (bookingNext.isPresent()) {
                NextBooking nextBooking = new NextBooking(bookingNext.get().getId(),
                        bookingNext.get().getBooker().getId());
                itemCommentsOutputDto.setNextBooking(nextBooking);
            }
        }

        return itemCommentsOutputDto;
    }

    private void setBooking(ItemOutputDto itemOutputDto, Optional<Booking> bookingNext, Optional<Booking> bookingLast) {
        if (bookingLast.isPresent()) {
            LastBooking lastBooking = new LastBooking(bookingLast.get().getId(), bookingLast.get().getBooker().getId());
            itemOutputDto.setLastBooking(lastBooking);
        }
        if (bookingNext.isPresent()) {
            NextBooking nextBooking = new NextBooking(bookingNext.get().getId(), bookingNext.get().getBooker().getId());
            itemOutputDto.setNextBooking(nextBooking);
        }
    }

    @Override
    public long deleteItem(long id) {
        itemRepository.deleteById(id);
        return id;
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemRepository.searchItems(text);
    }

    @Override
    public CommentOutputDto addCommentToItem(long userId, long itemId, CommentInputDto commentInputDto) {

        Optional<Item> item = itemRepository.findById(itemId);
        validationItem(item, itemId);

        Comment comment = new Comment();
        comment.setItem(item.get());

        Optional<User> user = userRepository.findById(userId);
        validationUser(user, userId);

        comment.setAuthor(user.get());

        List<Booking> bookingList = bookingRepository
                .getBookingByBookerAndEndIsBefore(user.get().getId(), LocalDateTime.now());

        Optional<Booking> bookingListFilterByItemId = bookingList.stream()
                .filter(p -> p.getItem().getId().equals(itemId))
                .findFirst();

        if (bookingListFilterByItemId.isEmpty()) {
            throw new UnavailableException(
                    String.format("Пользователь с id %d не брал в аренду вещь id = %d.", userId, itemId));
        }

        comment.setCreated(LocalDate.now());
        comment.setText(commentInputDto.getText());

        return ItemMapper.toCommentOutputDto(commentRepository.save(comment));
    }

    private void validationUser(Optional<User> user, long userId) {
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %x не существует.", userId));
        }
    }

    private void validationItem(Optional<Item> item, long itemId) {
        if (item.isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id = %x не существует.", itemId));
        }
    }
}