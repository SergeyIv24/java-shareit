package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserId(Long userId);

    List<Item> findByUserIdOrderByIdAsc(Long userId);

    List<Item> findByRequestId(Long requestId);

    @Query("select it " +
            "from Item as it " +
            "where (it.name ilike %?1% OR it.description ilike %?1%) " +
            "AND available = true")
    List<Item> findBySearch(String searchText);

}
