package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto addRequest(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(userId, itemRequestDto, LocalDateTime.now());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> getMyRequests(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemRequestService.getMyRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> getRequests(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                    @RequestParam(value = "from", defaultValue = "0") int from,
                                                    @RequestParam(value = "size", defaultValue = "0") int size) {
        return itemRequestService.getRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestResponseDto getRequestById(@PathVariable(value = "requestId") long requestId) {
        return itemRequestService.getRequestById(requestId);
    }
}
