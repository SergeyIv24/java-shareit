package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.List;


@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class ItemRequestServiceTest {

    private final EntityManager em;
    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private static User user;
    private static User userWithRequests;
    private static User anotherUser;

    private static ItemRequestDto requestDto1;
    private static ItemRequestDto req1;
    private static ItemRequestDto req2;
    private static ItemRequestDto req3;
    private static ItemRequestDto req4;
    private static ItemRequestDto req5;

    private static Item itemForReq3FromAnotherUser;

    private final LocalDateTime now = LocalDateTime.now();
    private final long badUserId = 10000L;

    @BeforeAll
    static void createUsers() {
        user = new User();
        user.setId(1L);
        user.setName("Test Testovich");
        user.setEmail("Test@Test.ru");

        userWithRequests = new User();
        userWithRequests.setName("SomeName");
        userWithRequests.setEmail("SomeEmail@some111.com");

        anotherUser = new User();
        anotherUser.setName("iiL");
        anotherUser.setEmail("II@gmail.com");
    }

    @BeforeAll
    static void createRequests() {
        requestDto1 = new ItemRequestDto();
        requestDto1.setId(1L);
        requestDto1.setUserId(user.getId());
        requestDto1.setDescription("Test Drrill");
        requestDto1.setCreated(LocalDateTime.of(2222, 12, 12, 12, 12, 12));

        req1 = new ItemRequestDto();
        req1.setDescription("Gime drill");

        req2 = new ItemRequestDto();
        req2.setDescription("I wanna take car");

        req3 = new ItemRequestDto();
        req3.setDescription("Find bee for me");

        req4 = new ItemRequestDto();
        req4.setDescription("Find brush for me");

        req5 = new ItemRequestDto();
        req5.setDescription("Find bicycle for me");
    }

    @BeforeAll
    static void createItems() {
        itemForReq3FromAnotherUser = new Item();
        itemForReq3FromAnotherUser.setName("Bee");
        itemForReq3FromAnotherUser.setDescription("My bee");
        itemForReq3FromAnotherUser.setAvailable(true);
    }


    @Test
    void shouldThrowNotFoundIfUserIsNotExisted() {
        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.addRequest(badUserId, requestDto1, now));
    }

    @Test
    void shouldAddRequest() {
        User returnedUser = userRepository.save(user);
        ItemRequestDto addedRequest = itemRequestService.addRequest(returnedUser.getId(), requestDto1, now);
        TypedQuery<ItemRequest> query =
                em.createQuery("SELECT ir FROM ItemRequest ir WHERE id = :id", ItemRequest.class);
        ItemRequest requestFromDataBase = query.setParameter("id", addedRequest.getId()).getSingleResult();

        assertThat(requestFromDataBase.getId(), notNullValue());
        assertThat(requestFromDataBase.getDescription(), equalTo(addedRequest.getDescription()));
        assertThat(requestFromDataBase.getUser().getId(), equalTo(addedRequest.getUserId()));
        assertThat(requestFromDataBase.getCreated(), equalTo(addedRequest.getCreated()));
    }

    @Test
    void shouldThrowNotFoundIfUserDoesNotHaveRequests() {
        User userWithoutRequests = new User();
        userWithoutRequests.setName("NoRequests");
        userWithoutRequests.setEmail("NoRequests@AtAll.com");

        User savedUserWithoutRequests = userRepository.save(userWithoutRequests);

        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.getMyRequests(badUserId));

        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.getMyRequests(savedUserWithoutRequests.getId()));
    }

    @Test
    void shouldGetUsersRequests() {
        User savedUserWithRequests = userRepository.save(userWithRequests);
        User savedAnotherUser = userRepository.save(anotherUser);

        ItemRequestDto savedReq1 = itemRequestService.addRequest(savedUserWithRequests.getId(), req1, now.minusDays(3));
        ItemRequestDto savedReq2 = itemRequestService.addRequest(savedUserWithRequests.getId(), req2, now.minusDays(15));
        ItemRequestDto savedReq3 = itemRequestService.addRequest(savedUserWithRequests.getId(), req3, now.minusDays(5));
        ItemRequestDto savedReq4 = itemRequestService.addRequest(savedAnotherUser.getId(), req4, now.minusDays(7));

        itemForReq3FromAnotherUser.setUser(savedAnotherUser);
        itemForReq3FromAnotherUser.setRequestId(savedReq3.getId());
        Item savedItemBee = itemRepository.save(itemForReq3FromAnotherUser);

        List<ItemRequestResponseDto> requests = itemRequestService.getMyRequests(savedUserWithRequests.getId());

        assertThat(requests.size(), equalTo(3));
        Assertions.assertFalse(requests.contains(savedReq4));
        assertThat(requests.get(0).getId(), equalTo(savedReq1.getId()));
        assertThat(requests.get(0).getDescription(), equalTo(savedReq1.getDescription()));
        assertThat(requests.get(0).getCreated(), equalTo(savedReq1.getCreated()));

        assertThat(requests.get(1).getId(), equalTo(savedReq3.getId()));
        assertThat(requests.get(1).getUserId(), equalTo(savedReq3.getUserId()));
        assertThat(requests.get(1).getDescription(), equalTo(savedReq3.getDescription()));
        assertThat(requests.get(1).getResponses().get(0).getId(), equalTo(savedItemBee.getId()));
        assertThat(requests.get(1).getResponses().get(0).getName(), equalTo(savedItemBee.getName()));
        assertThat(requests.get(1).getResponses().get(0).getOwnerId(), equalTo(savedItemBee.getUser().getId()));

        assertThat(requests.get(2).getId(), equalTo(savedReq2.getId()));
        assertThat(requests.get(2).getUserId(), equalTo(savedReq2.getUserId()));
        assertThat(requests.get(2).getCreated(), equalTo(savedReq2.getCreated()));
    }

    @Test
    void shouldThrowValidationExceptionIfFromOrSizeAreBad() {
        User user = new User();
        user.setName("Test");
        user.setEmail("UserTestAnother@Mail.com");
        User savedUser = userRepository.save(user);
        int badFrom = -1;
        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.getRequests(badFrom, 2, savedUser.getId()));

        int badSize = -1;
        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.getRequests(1, badSize, savedUser.getId()));

        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.getRequests(badFrom, badSize, savedUser.getId()));

        int failedUserId = 1000;
        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequests(0, 0, failedUserId));
    }

    @Test
    void shouldGetAllRequestExceptUsersRequests() {
        User savedUserWithRequests = userRepository.save(userWithRequests);
        User savedAnotherUser = userRepository.save(anotherUser);

        ItemRequestDto savedReq1 = itemRequestService.addRequest(savedUserWithRequests.getId(), req1, now.minusDays(3));
        ItemRequestDto savedReq2 = itemRequestService.addRequest(savedUserWithRequests.getId(), req2, now.minusDays(15));
        ItemRequestDto savedReq3 = itemRequestService.addRequest(savedUserWithRequests.getId(), req3, now.minusDays(5));
        ItemRequestDto savedReq4 = itemRequestService.addRequest(savedAnotherUser.getId(), req4, now.minusDays(7));
        ItemRequestDto savedReq5 = itemRequestService.addRequest(savedAnotherUser.getId(), req5, now.minusDays(100));

        List<ItemRequestResponseDto> anotherUsersRequests =
                itemRequestService.getRequests(1, 1, savedUserWithRequests.getId());

        assertThat(anotherUsersRequests.size(), equalTo(1));
        assertThat(anotherUsersRequests.get(0).getId(), equalTo(savedReq5.getId()));
        assertThat(anotherUsersRequests.get(0).getUserId(), equalTo(savedReq5.getUserId()));
        assertThat(anotherUsersRequests.get(0).getDescription(), equalTo(savedReq5.getDescription()));
        assertThat(anotherUsersRequests.get(0).getCreated(), equalTo(savedReq5.getCreated()));

        List<ItemRequestResponseDto> anotherUsersRequests1 =
                itemRequestService.getRequests(6, 50, savedUserWithRequests.getId());
        Assertions.assertTrue(anotherUsersRequests1.isEmpty());

        List<ItemRequestResponseDto> anotherUsersRequests2 =
                itemRequestService.getRequests(0, 1, savedUserWithRequests.getId());
        assertThat(anotherUsersRequests2.size(), equalTo(1));
        assertThat(anotherUsersRequests2.get(0).getId(), equalTo(savedReq4.getId()));
    }
}
