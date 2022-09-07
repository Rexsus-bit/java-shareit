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
import javax.validation.constraints.Min;
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
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public BookingDTO createBooking(@RequestHeader(header) long userId, @RequestBody @NonNull @Valid BookingDTO bookingDTO) {
        bookingDTO.setBookerId(userId);
        return mapper.toBookingDto(bookingService.createBooking(mapper.toBooking(bookingDTO), userId));
    }

    @PatchMapping("/{bookingId}")
    public Booking confirmBooking(@RequestHeader(header) long userId, @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.confirmBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader(header) long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    List<Booking> getAllBookings(@RequestHeader(header) long userId, @RequestParam(required = false, defaultValue = "ALL") State state,
                                 @Min(0) @RequestParam (required = false) Integer from,
                                 @Min(1) @RequestParam (required = false) Integer size) {
        return bookingService.getAllBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    List<Booking> getAllOwnerBookings(@RequestHeader(header) long userId, @RequestParam(defaultValue = "ALL") State state,
                                      @Min(0) @RequestParam (required = false) Integer from,
                                      @Min(1) @RequestParam (required = false) Integer size) {
        return bookingService.getAllOwnerBookings(userId, state, from, size);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorHandler(MethodArgumentTypeMismatchException ex) {
        Map<String, String> resp = new HashMap<>();
        resp.put("error", String.format("Unknown %s: %s", ex.getName(), ex.getValue()));
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);

    }
}
