package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.dto.RequestedItemsDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getRequestId() != null) {
            itemDto.setRequestId(item.getRequestId());
        }
        return itemDto;
    }

    public static Item mapToItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        if (itemDto.getRequestId() != null) {
            item.setRequestId(itemDto.getRequestId());
        }
        return item;
    }

    public static ItemDtoWithDates mapToItemDtoWithDates(Item item, Booking last,
                                                         Booking next,
                                                         List<CommentsDto> comments) {
        ItemDtoWithDates itemDtoWithDates = new ItemDtoWithDates();
        itemDtoWithDates.setId(item.getId());
        itemDtoWithDates.setName(item.getName());
        itemDtoWithDates.setDescription(item.getDescription());
        itemDtoWithDates.setAvailable(item.getAvailable());

        if (item.getRequestId() != null) {
            itemDtoWithDates.setRequestId(item.getRequestId());
        }
        if (last != null) {
            itemDtoWithDates.setLastBooking(BookingMapper.mapToBookingDtoDates(last));
        }
        if (next != null) {
            itemDtoWithDates.setNextBooking(BookingMapper.mapToBookingDtoDates(next));
        }
        if (comments != null) {
            itemDtoWithDates.setComments(comments);
        }
        return itemDtoWithDates;
    }

    public static RequestedItemsDto mapToRequestedItem(Item item) {
        RequestedItemsDto requestedItemsDto = new RequestedItemsDto();
        requestedItemsDto.setId(item.getId());
        requestedItemsDto.setName(item.getName());
        requestedItemsDto.setOwnerId(item.getUser().getId());
        return requestedItemsDto;
    }
}
