package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exceptions.NotExistedItemException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.TimeValidationException;
import ru.practicum.exceptions.UnavailableItemException;
import ru.practicum.item.ItemJpaRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserJpaRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;
    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @NonNull @Valid Booking booking) {
        booking.setBookerId(userId);
        return bookingService.create(booking);
    }

    @PatchMapping("/{bookingId}")
    public Booking confirmBooking(@PathVariable long bookingId, @RequestParam boolean approved){


        return bookingService.confirmBooking(bookingId, approved);
    }



}
