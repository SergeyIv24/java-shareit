package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Item;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static Item mapToItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDtoWithDates mapToItemDtoWithDates(Item item, Booking last, Booking next) {
        ItemDtoWithDates itemDtoWithDates = new ItemDtoWithDates();
        itemDtoWithDates.setId(item.getId());
        itemDtoWithDates.setName(item.getName());
        itemDtoWithDates.setDescription(item.getDescription());
        itemDtoWithDates.setAvailable(item.getAvailable());
        if (last == null && next == null) {
            return itemDtoWithDates;
        }
        itemDtoWithDates.setLastBooking(BookingMapper.mapToBookingDtoDates(last));
        itemDtoWithDates.setNextBooking(BookingMapper.mapToBookingDtoDates(next));
        return itemDtoWithDates;
    }
}
