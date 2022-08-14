package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

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
