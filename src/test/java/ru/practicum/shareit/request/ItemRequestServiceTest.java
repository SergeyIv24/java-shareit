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
@Rollback(value = false)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class ItemRequestServiceTest {
    private final EntityManager em;
    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;
    private static User user;
    private static ItemRequestDto requestDto1;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeAll
    static void create() {
        user = new User();
        user.setId(1L);
        user.setName("Test Testovich");
        user.setEmail("Test@Test.ru");

        requestDto1 = new ItemRequestDto();
        requestDto1.setId(1L);
        requestDto1.setUserId(user.getId());
        requestDto1.setDescription("Test Drrill");
        requestDto1.setCreated(LocalDateTime.of(2222,12,12,12,12,12));
    }


    @Test
    void shouldThrowNotFoundIfUserIsNotExisted() {
        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.addRequest(user.getId(), requestDto1, now));
    }

    @Test
    void shouldAddRequest() {
       User returnedUser = userRepository.save(user);
       ItemRequestDto addedRequest = itemRequestService.addRequest(returnedUser.getId(), requestDto1, now);
       TypedQuery<ItemRequest>  query =
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
        userWithoutRequests.setId(15L);
        userWithoutRequests.setName("NoRequests");
        userWithoutRequests.setEmail("NoRequests@AtAll.com");

        userRepository.save(userWithoutRequests);

        int failedUserId = 1000;
        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.getMyRequests(failedUserId));

        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.getMyRequests(userWithoutRequests.getId()));
    }

    @Test
    void shouldGetUsersRequests() {
        User userWithRequests = new User();
        userWithRequests.setId(20L);
        userWithRequests.setName("SomeName");
        userWithRequests.setEmail("SomeEmail@some.com");

        User anotherUser = new User();
        anotherUser.setId(30L);
        anotherUser.setName("iiL");
        anotherUser.setEmail("II@gmail.com");

        ItemRequestDto req1 = new ItemRequestDto();
        req1.setDescription("Gime drill");
        req1.setUserId(userWithRequests.getId());

        ItemRequestDto req2 = new ItemRequestDto();
        req2.setDescription("I wanna take car");
        req2.setUserId(userWithRequests.getId());

        ItemRequestDto req3 = new ItemRequestDto();
        req3.setDescription("Find bee for me");
        req3.setUserId(userWithRequests.getId());

        ItemRequestDto req4 = new ItemRequestDto();
        req3.setDescription("Find brush for me");
        req3.setUserId(anotherUser.getId());




        userRepository.save(userWithRequests);
        itemRequestService.addRequest(userWithRequests.getId(), req1, now.minusDays(3));
        itemRequestService.addRequest(userWithRequests.getId(), req2, now.minusDays(15));
        itemRequestService.addRequest(userWithRequests.getId(), req3, now.minusDays(5));
        itemRequestService.addRequest(anotherUser.getId(), req4, now.minusDays(7));

        List<ItemRequestResponseDto> requests = itemRequestService.getMyRequests(userWithRequests.getId());

        assertThat(requests.size(), equalTo(3));
/*        assertThat();
        assertThat();
        assertThat();*/


    }

    @Test
    void shouldThrowValidationExceptionIfFromOrSizeAreBad() {

        int badFrom = -1;
        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.getRequests(badFrom, 2, 1L));

        int badSize = -1;
        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.getRequests(1, badSize, 1L));

        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.getRequests(badFrom, badSize, 1L));

        int failedUserId = 1000;
        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.getRequests(0, 0, failedUserId));

    }

    @Test
    void shouldGetAllRequestExceptUsersRequests() {

    }
}
