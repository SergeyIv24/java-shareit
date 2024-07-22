package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImp implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper mapper;

    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto, LocalDateTime now) {
        User user = validateUser(userId);
        itemRequestDto.setUserId(userId);
        itemRequestDto.setCreated(now);
        return ItemRequestMapper
                .mapToItemRequestDto(requestRepository
                        .save(ItemRequestMapper.mapToItemRequest(itemRequestDto, user)));
    }

    @Override
    public List<ItemRequestResponseDto> getMyRequests(long userId) {
        validateUser(userId);
        List<ItemRequest> requests = requestRepository.findByUserIdOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            log.warn("User does not have any requests");
            throw new NotFoundException("User does not have any requests");
        }

        return requests
                .stream()
                .map(mapper::mapToItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponseDto> getRequests(int from, int size, long userId) {
        validateUser(userId);
        validatePageSize(from, size);
        if (from == 0 && size == 0) {
            return requestRepository.findByUserIdNotOrderByCreatedDesc(userId)
                    .stream()
                    .map(mapper::mapToItemRequestResponseDto)
                    .collect(Collectors.toList());
        }

        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from, size, sortByCreated);
        return requestRepository.findByUserIdNot(userId, pageable)
                .stream()
                .map(mapper::mapToItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestResponseDto getRequestById(long requestId) {
        Optional<ItemRequest> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            log.warn("request is not exist");
            throw new NotFoundException("request is not exist");
        }
        return mapper.mapToItemRequestResponseDto(request.get());
    }

    //If user exists, will return user, If does not - exception
    private User validateUser(long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return user.get();
        } else {
            log.warn("User is not existed");
            throw new NotFoundException("User is not existed");
        }
    }

    private void validatePageSize(int from, int size) {
        if (from < 0 || size < 0) {
            log.warn("Bad sizes. From = " + from + " " + "size = " + size);
            throw new ValidationException("Bad page`s size");
        }
    }
}
