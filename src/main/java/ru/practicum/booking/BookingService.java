package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.Mapper;
import ru.practicum.exceptions.AccessErrorException;
import ru.practicum.exceptions.NotExistedBookingException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.user.UserJpaRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingJpaRepository bookingRepository;
    private final UserJpaRepository userRepository;
    private final Mapper mapper;

    public Booking create(@Valid Booking booking, long userId) {

        if (booking.getStart().isBefore(LocalDateTime.now())
                || booking.getEnd().isBefore(LocalDateTime.now()))
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

        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwnerId()) {
            throw new AccessErrorException();
        }
        return booking;
    }

    public List<Booking> getAllBookings(long userId, State state) {
        if (!userRepository.existsById(userId)) throw new NotExistedUserException();
        List<Booking> bookingsList;
        switch (state) {

            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);

            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc
                        (userId, LocalDateTime.now());

            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc
                        (userId, LocalDateTime.now());

            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc
                        (userId, LocalDateTime.now(), LocalDateTime.now());

            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);

            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        }
        return null;
    }

    public List<Booking> getAllOwnerBookings(long userId, State state) {
        if (!userRepository.existsById(userId)) throw new NotExistedUserException();

//
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        }
        return null;

    }
}
