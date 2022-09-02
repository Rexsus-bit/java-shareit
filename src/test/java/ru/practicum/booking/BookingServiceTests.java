package ru.practicum.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exceptions.AccessErrorException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.user.User;
import ru.practicum.user.UserJpaRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {

    @Mock
    private BookingJpaRepository bookingRepository;
    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    User owner = new User(1L, "name", "useremail@gmail.com");
    User booker = new User(2L, "name", "bookeremail@gmail.com\"");
    Item item = new Item(1L, "name", "description", true, 1L, 1L);
    LocalDateTime startTime = LocalDateTime.now().plusMinutes(5).truncatedTo(ChronoUnit.SECONDS);
    LocalDateTime endTime = LocalDateTime.now().plusMinutes(10).truncatedTo(ChronoUnit.SECONDS);

    Booking booking = Booking.builder()
            .id(1L)
            .start(startTime)
            .end(endTime)
            .item(item)
            .booker(booker)
            .status(Status.WAITING)
            .build();

    @Test
    public void shouldCreateBookingTest() {
        when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
                .thenAnswer(invoc -> invoc.getArguments()[0]);

        Booking createdBooking = bookingService.createBooking(booking, booker.getId());

        assertThat(createdBooking, notNullValue());
        assertThat(createdBooking, hasProperty("start", equalTo(startTime)));
        assertThat(createdBooking, hasProperty("end", equalTo(endTime)));
        assertThat(createdBooking, hasProperty("item", equalTo(item)));
        assertThat(createdBooking, hasProperty("booker", equalTo(booker)));
        assertThat(createdBooking, hasProperty("status", equalTo(Status.WAITING)));
    }

    @Test
    public void shouldFailCreateBookingByOwnerTest() {
        Assertions.assertThrows(AccessErrorException.class,
                () -> bookingService.createBooking(booking, owner.getId()));
    }

    @Test
    public void shouldFailCreateBookingInPastTest() {
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.createBooking(new Booking(2L,
                                LocalDateTime.now().minusMinutes(2),
                                LocalDateTime.now().minusMinutes(1), null, null, null),
                        booker.getId()));
    }

    @Test
    public void shouldConfirmBookingTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking approvedBooking = bookingService
                .confirmBooking(owner.getId(), booking.getId(), true);

        assertThat(approvedBooking, notNullValue());
        assertThat(approvedBooking, hasProperty("start", equalTo(startTime)));
        assertThat(approvedBooking, hasProperty("end", equalTo(endTime)));
        assertThat(approvedBooking, hasProperty("item", equalTo(item)));
        assertThat(approvedBooking, hasProperty("booker", equalTo(booker)));
        assertThat(approvedBooking, hasProperty("status", equalTo(Status.APPROVED)));
    }

    @Test
    public void shouldDeclineBookingTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking rejectedBooking = bookingService
                .confirmBooking(owner.getId(), booking.getId(), false);

        assertThat(rejectedBooking, notNullValue());
        assertThat(rejectedBooking, hasProperty("start", equalTo(startTime)));
        assertThat(rejectedBooking, hasProperty("end", equalTo(endTime)));
        assertThat(rejectedBooking, hasProperty("item", equalTo(item)));
        assertThat(rejectedBooking, hasProperty("booker", equalTo(booker)));
        assertThat(rejectedBooking, hasProperty("status", equalTo(Status.REJECTED)));
    }

    @Test
    public void shouldFailConfirmBookingWhichHasBeenApprovedTest() {
        booking.setStatus(Status.APPROVED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(ValidationException.class,
                () -> bookingService
                        .confirmBooking(owner.getId(), booking.getId(), true));
    }

    @Test
    public void shouldFailConfirmBookingByUnauthorisedUserTest() {
        booking.setStatus(Status.APPROVED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(AccessErrorException.class,
                () -> bookingService
                        .confirmBooking(10L, booking.getId(), true));
    }

    @Test
    public void shouldGetBookingByIdTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        Booking foundBooking = bookingService.getBookingById(booking.getItem().getOwnerId(), booking.getId());

        assertThat(foundBooking, notNullValue());
        assertThat(foundBooking, hasProperty("start", equalTo(startTime)));
        assertThat(foundBooking, hasProperty("end", equalTo(endTime)));
        assertThat(foundBooking, hasProperty("item", equalTo(item)));
        assertThat(foundBooking, hasProperty("booker", equalTo(booker)));
        assertThat(foundBooking, hasProperty("status", equalTo(Status.WAITING)));
    }

    @Test
    public void shouldFailGetBookingByIdTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(AccessErrorException.class,
                () -> bookingService.getBookingById(10L, booking.getId()));
    }

    @Test
    public void shouldGetAllBookingsTest() {
        List<Booking> list = List.of(booking);

        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);

        assertThat(bookingService.getAllBookings(1L, State.ALL, 0, 5), equalTo(list));
        assertThat(bookingService.getAllBookings(1L, State.PAST, 0, 5), equalTo(list));
        assertThat(bookingService.getAllBookings(1L, State.CURRENT, 0, 5), equalTo(list));
        assertThat(bookingService.getAllBookings(1L, State.FUTURE, 0, 5), equalTo(list));
        assertThat(bookingService.getAllBookings(1L, State.WAITING, 0, 5), equalTo(list));
        assertThat(bookingService.getAllBookings(1L, State.REJECTED, 0, 5), equalTo(list));
    }

    @Test
    public void shouldGetAllOwnerBookingsTest() {
        List<Booking> list = List.of(booking);

        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(list);

        assertThat(bookingService.getAllOwnerBookings(1L, State.ALL, 0, 5),
                equalTo(list));
        assertThat(bookingService.getAllOwnerBookings(1L, State.PAST, 0, 5),
                equalTo(list));
        assertThat(bookingService.getAllOwnerBookings(1L, State.CURRENT, 0, 5),
                equalTo(list));
        assertThat(bookingService.getAllOwnerBookings(1L, State.FUTURE, 0, 5),
                equalTo(list));
        assertThat(bookingService.getAllOwnerBookings(1L, State.WAITING, 0, 5),
                equalTo(list));
        assertThat(bookingService.getAllOwnerBookings(1L, State.REJECTED, 0, 5),
                equalTo(list));
    }

}

