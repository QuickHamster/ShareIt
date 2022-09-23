package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.LastBooking;
import ru.practicum.shareit.booking.model.NextBooking;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    //private TreeSet<Task> sortTasks = new TreeSet<Task>(new BookingComparator()); // сортированные задачи и подзадачи

   /* private class BookingComparator implements Comparator<ItemOutputDto> {

        public int compare(ItemOutputDto i1, ItemOutputDto i2) {
            if ((i1 == null) || (i2 == null)) {
                return 0;
            } else if (i1.getId() == null) {
                return 1;
            } else if (i2.getId() == null) {
                return -1;
            } else return (i1.getId() > i2.getId()) ? 1 : -1;
        }
    }*/

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
        userRepository.findAll().stream()
                .filter(p -> p.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не существует.", userId)));
        itemDto.setOwner(userRepository.findById(userId).get());
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
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

        return ItemMapper.toItemDto(item.get());
    }

    @Override
    public ItemCommentsOutputDto findItemById(long userId, long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundException(String.format("Вещи с id %x не существует.", id));
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь # %d не найден.", userId));
        }
        List<Comment> comments = commentRepository.findCommentsByAuthorAndItem(userId, id);
        //List<Booking> bookingsItem = bookingRepository.getBookingsByItem(id);
        ItemCommentsOutputDto itemCommentsOutputDto = ItemMapper.toItemCommentsOutputDto(item.get(), comments);
        if (item.get().getOwner().getId().equals(userId)) {
            Optional<Booking> bookingNext = bookingRepository.findFirstByItemOrderByEndDesc(item.get());
            Optional<Booking> bookingLast = bookingRepository.findFirstByItemOrderByStartAsc(item.get());
            if (bookingLast.isPresent()) {
                LastBooking lastBooking = new LastBooking(bookingLast.get().getId(), bookingLast.get().getBooker().getId());
                itemCommentsOutputDto.setLastBooking(lastBooking);
            }
            if (bookingNext.isPresent()) {
                NextBooking nextBooking = new NextBooking(bookingNext.get().getId(), bookingNext.get().getBooker().getId());
                itemCommentsOutputDto.setNextBooking(nextBooking);
            }
        }
        if (!comments.isEmpty()) {
            itemCommentsOutputDto.setComments(comments);
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
        if (!text.isBlank()) {
            return itemRepository.searchItems(text);
        } else return new ArrayList<>();
    }

    @Override
    public CommentOutputDto addCommentToItem(long userId, long itemId, CommentInputDto commentInputDto) {

        Optional<Item> item = itemRepository.findById(itemId);
        Comment comment = new Comment();
        if (item.isPresent()) {
            comment.setItem(item.get());
        } else throw new NotFoundException(String.format("Вещь с id %d не существует.", itemId));

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            comment.setAuthor(user.get());
        } else throw new NotFoundException(String.format("Пользователь с id %d не существует.", userId));

        List<Booking> bookingList = bookingRepository
                .getBookingByBookerAndEndIsBefore(user.get().getId(), LocalDateTime.now());

        Optional<Booking> bookingList1 = bookingList.stream()
                .filter(p -> p.getItem().getId().equals(itemId))
                .findFirst();

        if (bookingList1.isEmpty()) {
            throw new UnavailableException(
                    String.format("Пользователь с id %d не брал в аренду вещь id = %d.", userId, itemId));
        }
               /* .orElseThrow(() -> new UnavailableException(
                        String.format("Пользователь с id %d не брал в аренду вещь id = %d.", userId, itemId)));
        /*bookingRepository
                .findBookingByBookerAfterAndEnd(user.get(), LocalDateTime.now()).stream()
                .filter(p -> p.getItem().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new UnavailableException(
                        String.format("Пользователь с id %d не брал в аренду вещь id = %d.", userId, itemId)));*/

        comment.setCreated(LocalDate.now());
        comment.setText(commentInputDto.getText());

        //commentRepository.save(comment);

        return ItemMapper.toCommentOutputDto(commentRepository.save(comment));
    }
}
