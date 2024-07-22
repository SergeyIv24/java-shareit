package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        checkUser(userId);
        User itemOwner = userRepository.findById(userId).orElseThrow();
        Item addingItem = ItemMapper.mapToItem(itemDto);
        addingItem.setUser(itemOwner);
        return ItemMapper.mapToItemDto(itemRepository.save(addingItem));
    }

    @Override
    public ItemDto updateItem(long itemId, long ownerId, ItemDto item) {
        checkUser(ownerId);
        checkUsersItems(itemId, ownerId);

        Item updatingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found"));

        if (item.getName() != null) {
            updatingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatingItem.setAvailable(item.getAvailable());
        }

        return ItemMapper.mapToItemDto(itemRepository.save(updatingItem));
    }

    @Override
    public ItemDtoWithDates getItemById(long itemId, LocalDateTime now, long ownerId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item is not found"));
        List<CommentsDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
        if (item.getUser().getId().equals(ownerId)) {
            Booking lastBooking = bookingRepository.findLastBooking(itemId, now).orElse(null);
            Booking nextBooing = bookingRepository.findNextBooking(itemId, now).orElse(null);
            return ItemMapper.mapToItemDtoWithDates(item, lastBooking, nextBooing, comments);
        }
        return ItemMapper.mapToItemDtoWithDates(item, null, null, comments);
    }

    @Override
    public Collection<ItemDtoWithDates> getMyItems(long userId, LocalDateTime now) {
        List<Item> items = itemRepository.findByUserIdOrderByIdAsc(userId);
        List<ItemDtoWithDates> itemsWithDates = new ArrayList<>();

        for (Item item : items) {
            itemsWithDates.add(getItemById(item.getId(), now, userId));
        }
        return itemsWithDates;
    }

    @Override
    public Collection<ItemDto> searchByRequest(String text) {
        if (text.isEmpty()) {
            return List.of();
        }

        List<Item> searchedItems = itemRepository.findBySearch(text);

        if (searchedItems.isEmpty()) {
            throw new NotFoundException("Item is not found");
        }
        return searchedItems.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentsDto addComment(long userId, CommentsDto commentsDto, long itemId) {
        User user = checkUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("item is not found"));

        List<Booking> bookingsByItemId = bookingRepository.findByBookerIdAndItemIdAndStatus(userId,
                        itemId,
                        String.valueOf(BookingStatus.APPROVED))
                .stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .toList();

        if (bookingsByItemId.isEmpty()) {
            throw new ValidationException("Booking is not started yet");
        }

        Comments comment = CommentMapper.mapToComments(commentsDto, item, user);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }


    private User checkUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user is not found"));
    }

    private void checkUsersItems(long itemId, long ownerId) {
        Item existedItem = itemRepository.findById(itemId)
                .orElseThrow();
        if (!existedItem.getUser().getId().equals(ownerId)) {
            throw new NotFoundException("Item is not found");
        }
    }
}
