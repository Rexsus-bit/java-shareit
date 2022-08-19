package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.AccessErrorException;
import ru.practicum.exceptions.NotExistedBookingException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDTO;
import ru.practicum.item.ItemJpaRepository;
import ru.practicum.user.UserJpaRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    final LocalDateTime CURRENT_TIME = LocalDateTime.now();
    private final BookingJpaRepository bookingRepository;
    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;

    public Booking create(@Valid Booking booking, long userId) {

        if (booking.getStart().isBefore(CURRENT_TIME)
                || booking.getEnd().isBefore(CURRENT_TIME))
            throw new ValidationException();
        if (userId == booking.getItem().getOwnerId()) {
            throw new AccessErrorException();
        }

        bookingRepository.save(booking);
        return bookingRepository.getById(booking.getId());
    }

    public Booking confirmBooking(long userId, long bookingId, boolean approval) {

        Booking booking = bookingRepository.getById(bookingId);
        if (userId != booking.getItem().getOwnerId()) {
            throw new AccessErrorException();
        }
        if (booking.getStatus() == Status.APPROVED) {
            throw new ValidationException();
        }
        if (approval) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(long userId, long bookingId) {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotExistedBookingException::new);

            if(userId != booking.getBooker().getId() && userId != booking.getItem().getOwnerId()) {
                throw new AccessErrorException();
            }
            return booking;
    }

    public List<Booking> getAllBookings(long userId, State state) {
        if (!userRepository.existsById(userId)) throw new NotExistedUserException();
        switch (state){

            case ALL:
        return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);

            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, CURRENT_TIME);

            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, CURRENT_TIME);

            case CURRENT:
                break;

            case WAITING:
                break;

            case REJECTED:
                break;

        }

        return null;
    }

    public List<Booking> getAllOwnerBookings(long userId, State state) {
        if (!userRepository.existsById(userId)) throw new NotExistedUserException();
       return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
    }
}
