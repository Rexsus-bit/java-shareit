package ru.practicum.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingJpaRepository;
import ru.practicum.booking.BookingLinksDTO;
import ru.practicum.booking.Status;
import ru.practicum.common.Mapper;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.user.User;
import ru.practicum.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

    @Mock
    private ItemJpaRepository itemRepository;

    @Mock
    private CommentJpaRepository commentRepository;
    @Mock
    private BookingJpaRepository bookingRepository;
    @Mock
    Mapper mapper;

    User user = new User(1L, "James", "james@yandex.com");
    UserDTO userDTO = new UserDTO(1L, "James", "james@yandex.com");

    Item item1 = Item.builder()
            .id(1L)
            .name("name")
            .description("отверТка")
            .available(true)
            .ownerId(userDTO.getId())
            .build();
    Item item2 = Item.builder().name("2")
            .description("молоток")
            .available(true)
            .ownerId(userDTO.getId())
            .build();
          Booking bookingInPast = new Booking(1L, LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(2), item1, user, Status.APPROVED);
          Booking bookingInFuture = new Booking(2L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(20), item1, user, Status.APPROVED);
          Comment comment = new Comment(1L,"text", item1, user);

    @InjectMocks
    private ItemService itemService;

    @Test
    public void shouldOwnerGetItemById() throws NotExistedUserException {
        ItemDTO item1DTO = ItemDTO.builder()
                .id(1L)
                .name("name")
                .description("отверТка")
                .available(true)
                .build();

        LocalDateTime currentTime = LocalDateTime.now();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.findAllByItemIdAndEndIsBeforeOrderByEndDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(bookingInPast));
        when(bookingRepository.findAllByItemIdAndStartIsAfterOrderByStartAsc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(bookingInFuture));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));
        when(mapper.toItemDTO(any(Item.class))).thenReturn(item1DTO);


       itemService.getItem(item1.getId(),item1.getOwnerId());

        ItemDTO expectedItemDTO = new ItemDTO(1L, "name", "отверТка", true, new BookingLinksDTO(1L,
                bookingInPast.getBooker().getId()),new BookingLinksDTO(2L,
                bookingInFuture.getBooker().getId()), Set.of(new CommentDTO(1L,"text", "name", currentTime)), null);

        assertThat(expectedItemDTO, notNullValue());
        assertThat(expectedItemDTO, hasProperty("name", equalTo(item1.getName())));
        assertThat(expectedItemDTO, hasProperty("description", equalTo(item1.getDescription())));
        assertThat(expectedItemDTO, hasProperty("lastBooking", equalTo(new BookingLinksDTO(bookingInPast.getId(),bookingInPast.getBooker().getId()))));
        assertThat(expectedItemDTO, hasProperty("nextBooking", equalTo(new BookingLinksDTO(bookingInFuture.getId(),bookingInFuture.getBooker().getId()))));
        assertThat(expectedItemDTO, hasProperty("comments", equalTo(Set.of(new CommentDTO(1L,"text", "name", currentTime)))));

    }




}
