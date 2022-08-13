package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.common.Mapper;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final Mapper mapper;

    @PostMapping
    public BookingDTO createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @NonNull @Valid BookingDTO bookingDTO) {

        bookingDTO.setBookerId(userId);
        return mapper.toBookingDto(bookingService.create(mapper.toBooking(bookingDTO), userId));
    }

    @PatchMapping("/{bookingId}")
    public Booking confirmBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.confirmBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    List<Booking> getAllBookings(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllBookings(userId, state);
    }

    @GetMapping("/owner")
    List<Booking> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllOwnerBookings(userId, state);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorHandler(MethodArgumentTypeMismatchException ex) {
        Map<String, String> resp = new HashMap<>();
        resp.put("error", String.format("Unknown %s: %s", ex.getName(), ex.getValue()));
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);

    }
}
