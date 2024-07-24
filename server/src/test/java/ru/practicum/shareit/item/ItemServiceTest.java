package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

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

    private static User userAlex;
    private static User userLeonardo;

    private static ItemDto userAlexsItemDtoHammer;
    private static ItemDto userAlexsItemDtoPot;
    private static ItemDto userAlexsItemDtoTable;
    private static ItemDto userLeonardsItemDtoBox;

    @BeforeAll
    static void setup() {
        userAlex = new User();
        userAlex.setName("Alex");
        userAlex.setEmail("Alex@Alex.ru");

        userLeonardo = new User();
        userLeonardo.setName("Leonard");
        userLeonardo.setEmail("Leonard@gmail.ru");

        userAlexsItemDtoHammer = new ItemDto();
        userAlexsItemDtoHammer.setName("Hammer");
        userAlexsItemDtoHammer.setDescription("New good hammer");
        userAlexsItemDtoHammer.setAvailable(true);

        userAlexsItemDtoPot = new ItemDto();
        userAlexsItemDtoPot.setName("Pot");
        userAlexsItemDtoPot.setDescription("For tea");
        userAlexsItemDtoPot.setAvailable(true);

        userAlexsItemDtoTable = new ItemDto();
        userAlexsItemDtoTable.setName("Table");
        userAlexsItemDtoTable.setDescription("for eating");
        userAlexsItemDtoTable.setAvailable(true);

        userLeonardsItemDtoBox = new ItemDto();
        userLeonardsItemDtoBox.setName("Box");
        userLeonardsItemDtoBox.setDescription("to store things");
        userLeonardsItemDtoBox.setAvailable(true);
    }

    @Test
    void shouldReturnItemByIdAfterCreatingAndUpdating() {
        User savedUser = userRepository.save(userAlex);
        userAlexsItemDtoHammer.setName("Hammer");
        ItemDto savedItem = itemService.addItem(savedUser.getId(), userAlexsItemDtoHammer);

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
        User savedUser1 = userRepository.save(userAlex);
        User savedUser2 = userRepository.save(userLeonardo);

        ItemDto savedItem1User1 = itemService.addItem(savedUser1.getId(), userAlexsItemDtoHammer);
        ItemDto savedItem2User1 = itemService.addItem(savedUser1.getId(), userAlexsItemDtoPot);
        ItemDto savedItem3User1 = itemService.addItem(savedUser1.getId(), userAlexsItemDtoTable);

        //User2 does not have items
        assertThat(itemService.getMyItems(savedUser2.getId(), LocalDateTime.now()), equalTo(List.of()));

        List<ItemDtoWithDates> users1Items = itemService.getMyItems(savedUser1.getId(), LocalDateTime.now())
                .stream().toList();

        assertThat(users1Items.size(), equalTo(3));
        assertThat(users1Items.get(0).getId(), equalTo(savedItem1User1.getId()));
        assertThat(users1Items.get(0).getName(), equalTo(savedItem1User1.getName()));
        assertThat(users1Items.get(0).getDescription(), equalTo(savedItem1User1.getDescription()));
        assertThat(users1Items.get(0).getAvailable(), equalTo(savedItem1User1.getAvailable()));
        assertThat(users1Items.get(1).getId(), equalTo(savedItem2User1.getId()));
        assertThat(users1Items.get(1).getName(), equalTo(savedItem2User1.getName()));
        assertThat(users1Items.get(2).getName(), equalTo(savedItem3User1.getName()));
    }

    @Test
    void shouldFindItem() {
        User savedUser1 = userRepository.save(userAlex);

        userAlexsItemDtoHammer.setName("FiNd   Me");
        userAlexsItemDtoPot.setDescription("FIND me too");

        ItemDto savedItem1User1 = itemService.addItem(savedUser1.getId(), userAlexsItemDtoHammer);
        ItemDto savedItem2User1 = itemService.addItem(savedUser1.getId(), userAlexsItemDtoPot);
        ItemDto savedItem3User1 = itemService.addItem(savedUser1.getId(), userAlexsItemDtoTable);

        List<ItemDto> searchedItems = itemService.searchByRequest("FInd").stream().toList();

        assertThat(searchedItems.size(), equalTo(2));
        assertThat(searchedItems.get(0).getId(), equalTo(savedItem1User1.getId()));
        assertThat(searchedItems.get(0).getName(), equalTo(savedItem1User1.getName()));
        assertThat(searchedItems.get(0).getDescription(), equalTo(savedItem1User1.getDescription()));
        assertThat(searchedItems.get(1).getId(), equalTo(savedItem2User1.getId()));
        assertThat(searchedItems.get(1).getName(), equalTo(savedItem2User1.getName()));
        assertThat(searchedItems.get(1).getDescription(), equalTo(savedItem2User1.getDescription()));

        Assertions.assertThrows(NotFoundException.class,
                () -> itemService.searchByRequest("There are not items"));
    }
}
