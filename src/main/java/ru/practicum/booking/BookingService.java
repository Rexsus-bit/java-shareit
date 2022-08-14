package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotExistedItemException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.TimeValidationException;
import ru.practicum.exceptions.UnavailableItemException;
import ru.practicum.item.ItemJpaRepository;
import ru.practicum.user.UserJpaRepository;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingJpaRepository bookingRepository;
    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;

    public Booking create(@Valid Booking booking) {
        LocalDateTime CURRENT_TIME = LocalDateTime.now();

        if (booking.getStart().isBefore(CURRENT_TIME)
                || booking.getEnd().isBefore(CURRENT_TIME))
            throw new TimeValidationException();
        if (!itemRepository.existsById(booking.getItemId())) throw new NotExistedItemException();
        if (!userRepository.existsById(booking.getBookerId())) throw new NotExistedUserException();
        if (!itemRepository.getById(booking.getItemId()).getAvailable()) throw new UnavailableItemException();


        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(long bookingId, boolean approval) {
        Booking booking = bookingRepository.getById(bookingId);
        if (approval) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(booking);
    }
}
