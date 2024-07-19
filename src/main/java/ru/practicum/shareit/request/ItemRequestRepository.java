package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByUserIdOrderByCreatedDesc(long userId);

    //Find all request without belonging to user who sent request
    List<ItemRequest> findByUserIdNotOrderByCreatedDesc(long userId);

    List<ItemRequest> findByUserIdNotAndIdGreaterThanEqualOrderByCreatedDesc(long userId, int from);

    List<ItemRequest> findByUserIdNot(long userId, Pageable pageable);
}
