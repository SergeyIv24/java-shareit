package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserId(Long userId);

    //List<Item> findByNameContainingIgnoreCase(String searchText); //OrDescriptionContainingIgnoreCase

    @Query("select it " +
            "from Item as it " +
            "where (it.name ilike %?1% OR it.description ilike %?1%) " +
            "AND available = true")

    List<Item> findBySearch(String searchText);


/*    Item addItem(Long userId, ItemDto itemDto);

    Item updateItem(Long itemId, ItemDto item);

    Optional<Item> getItemById(Long itemId);

    Collection<Item> getMyItems(Long userId);

    Collection<Item> searchByText(String text);*/

    /*    @Query(value = "select * from items " +
            "where (name ilike  ?1% OR description ilike ?1%) " +
            "AND available = true", nativeQuery = true)*/
}
