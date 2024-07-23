package ru.practicum.shareit.requests;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestsClient itemRequestsClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addRequest(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestsClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getMyRequests(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemRequestsClient.getMyRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequests(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                              @RequestParam(value = "from", defaultValue = "0") int from,
                                              @RequestParam(value = "size", defaultValue = "0") int size) {
        validatePageSize(from, size);
        return itemRequestsClient.getRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestById(@PathVariable(value = "requestId") long requestId) {
        return itemRequestsClient.getRequestById(requestId);
    }

    private void validatePageSize(int from, int size) {
        if (from < 0 || size < 0) {
            log.warn("Bad sizes. From = " + from + " " + "size = " + size);
            throw new ValidationException("Bad page`s size");
        }
    }
}
