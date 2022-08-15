package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotExistedItemException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.TimeValidationException;
import ru.practicum.exceptions.UnavailableItemException;
import ru.practicum.item.ItemJpaRepository;
import ru.practicum.user.UserJpaRepository;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {
    final LocalDateTime CURRENT_TIME = LocalDateTime.now();
    private final BookingJpaRepository bookingRepository;
    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;

    public Booking create(@Valid Booking booking, long userId) {
//        LocalDateTime CURRENT_TIME

        if (booking.getStart().isBefore(CURRENT_TIME)
                || booking.getEnd().isBefore(CURRENT_TIME))
            throw new TimeValidationException();
        if (booking.getBookedItem() == null) throw new NotExistedItemException();
        if (!booking.getBookedItem().getAvailable()) throw new UnavailableItemException();
       try{
           booking.setBooker(userRepository.getById(userId));
       } catch (EntityNotFoundException e){
           throw new NotExistedUserException();
       }
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
