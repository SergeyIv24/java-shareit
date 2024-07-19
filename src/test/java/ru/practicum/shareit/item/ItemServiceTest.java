package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserRepository userRepository;

    private static User user1;

    private static ItemDto itemDto1User1;
    private static ItemDto itemDto2User1;
    private static ItemDto itemDto3User1;
    private static ItemDto itemDto4User2;

    @BeforeAll
    static void createUsers() {
        user1 = new User();
        user1.setName("Alex");
        user1.setEmail("Alex@Alex.ru");

    }

    @BeforeAll
    static void createItems() {
        itemDto1User1 = new ItemDto();
        itemDto1User1.setName("Hammer");
        itemDto1User1.setDescription("New good hammer");
        itemDto1User1.setAvailable(true);
    }

    @Test
    void shouldReturnItemByIdAfterCreatingAndUpdating() {
        User savedUser = userRepository.save(user1);
        ItemDto savedItem = itemService.addItem(savedUser.getId(), itemDto1User1);

        assertThat(savedItem.getId(), notNullValue());
        assertThat(savedItem.getName(), equalTo("Hammer"));
        assertThat(savedItem.getDescription(), equalTo("New good hammer"));
        Assertions.assertTrue(savedItem.getAvailable());

        savedItem.setName("Drill");
        savedItem.setDescription("Old drill");

        ItemDto updatedItem = itemService.updateItem(savedItem.getId(), savedUser.getId(), savedItem);

        assertThat(updatedItem.getId(), notNullValue());
        assertThat(updatedItem.getName(), equalTo("Drill"));
        assertThat(updatedItem.getDescription(), equalTo("Old drill"));
        assertThat(updatedItem.getId(), equalTo(savedItem.getId()));
        Assertions.assertTrue(updatedItem.getAvailable());

        ItemDto gotItem = itemService.getItemById(updatedItem.getId(), LocalDateTime.now(), savedUser.getId());

        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo("Drill"));
        assertThat(gotItem.getDescription(), equalTo("Old drill"));
        assertThat(gotItem.getId(), equalTo(savedItem.getId()));
        Assertions.assertTrue(gotItem.getAvailable());

    }

    @Test
    void shouldReturnUsersItems() {


    }

    @Test
    void shouldFindItem() {

    }


}
