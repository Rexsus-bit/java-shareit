package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingJpaRepository;
import ru.practicum.booking.BookingLinksDTO;
import ru.practicum.booking.BookingService;
import ru.practicum.user.User;
import ru.practicum.user.UserDTO;
import ru.practicum.user.UserService;
import ru.practicum.util.DataBaseCleaner;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTests {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final BookingJpaRepository bookingJpaRepository;

    private final DataBaseCleaner dataBaseCleaner;

    User user;
    UserDTO userDTO;
    Item item1;
    Item item2;


    @BeforeEach
    void beforeTest() {
        dataBaseCleaner.clean();

        user = new User(1L, "James", "james@yandex.com");
        userDTO = userService.create(user);
        item1 = Item.builder().name("1")
                .description("отверТка")
                .available(true)
                .ownerId(userDTO.getId())
                .build();
        item2 = Item.builder().name("2")
                .description("молоток")
                .available(true)
                .ownerId(userDTO.getId())
                .build();
        item1 = itemService.create(item1, userDTO.getId());
        item2 = itemService.create(item2, userDTO.getId());

    }

    @Test
    public void shouldGetAllUserItems() {
        assertThat(userDTO, hasProperty("id", equalTo(1L)));



        assertThat(itemService.getAllUserItems(1L, 0, 3), equalTo(List.of(itemService.getItem(1,2), itemService.getItem(2,2))));

        User userBooker = User.builder()
                .name("name")
                .email("user@tt.tu")
                .build();
        UserDTO userBookerDTO = userService.create(userBooker);

        Booking booking1ForItem1 = Booking.builder()
                .item(item1)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .booker(userBooker)
                .build();
        booking1ForItem1 = bookingService.createBooking(booking1ForItem1, userBookerDTO.getId());

        Booking booking2ForItem1 = Booking.builder()
                .item(item1)
                .start(LocalDateTime.now().plusMinutes(5))
                .end(LocalDateTime.now().plusMinutes(6))
                .booker(userBooker)
                .build();
        bookingService.createBooking(booking2ForItem1, userBookerDTO.getId());

        Booking booking1ForItem2 = Booking.builder()
                .item(item2)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .booker(userBooker)
                .build();
        booking1ForItem2 = bookingService.createBooking(booking1ForItem2, userBookerDTO.getId());

        Booking booking2ForItem2 = Booking.builder()
                .item(item2)
                .start(LocalDateTime.now().plusMinutes(3))
                .end(LocalDateTime.now().plusMinutes(4))
                .booker(userBooker)
                .build();
        bookingService.createBooking(booking2ForItem2, userBookerDTO.getId());

        List<ItemDTO> allUserItemsList = itemService.getAllUserItems(1, 0, 5);

        assertThat(allUserItemsList, hasSize(2));
        assertThat(allUserItemsList.get(0).getId(), equalTo(item1.getId()));
        assertThat(allUserItemsList.toArray()[0],
                hasProperty("nextBooking",
                        equalTo(new BookingLinksDTO(booking1ForItem1.getId(),booking1ForItem1.getBooker().getId()))));
        assertThat(allUserItemsList.toArray()[0], hasProperty("name"));
        assertThat(allUserItemsList.get(1).getId(), equalTo(item2.getId()));
        assertThat(allUserItemsList.toArray()[1],
                hasProperty("nextBooking",
                        equalTo(new BookingLinksDTO(booking1ForItem2.getId(),booking1ForItem2.getBooker().getId()))));
        assertThat(allUserItemsList.toArray()[1], hasProperty("name"));
    }

    @Test
    void shouldFindItems() {
        assertThat(itemService.searchAvailableItems("ОТВЕРТКА"), equalTo(List.of(item1)));
    }

}
