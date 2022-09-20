package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.common.Mapper;
import ru.practicum.exceptions.AccessErrorException;
import ru.practicum.exceptions.NotExistedBookingException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.user.UserJpaRepository;
import ru.practicum.util.OffsetLimitPageable;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingJpaRepository bookingRepository;
    private final UserJpaRepository userRepository;
    private final Mapper mapper;

    public Booking createBooking(@Valid Booking booking, long userId) {

        if (booking.getStart().isBefore(LocalDateTime.now())
                || booking.getEnd().isBefore(LocalDateTime.now()))
            throw new ValidationException();
        if (userId == booking.getItem().getOwnerId()) {
            throw new AccessErrorException();
        }
        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(long ownerId, long bookingId, boolean approval) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotExistedUserException:: new);
        if (ownerId != booking.getItem().getOwnerId()) {
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

    public List<Booking> getAllBookings(long userId, State state, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) throw new NotExistedUserException();
        LocalDateTime currentTime = LocalDateTime.now();
        Pageable page = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.DESC, "start"));
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, currentTime, page);
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, currentTime, page);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, currentTime, currentTime, page);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
            default: return null;
        }
    }

    public List<Booking> getAllOwnerBookings(long userId, State state, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotExistedUserException();
        }
        LocalDateTime currentTime = LocalDateTime.now();
        Pageable page = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.DESC, "start"));
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, page);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, currentTime, page);
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, currentTime, page);
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, currentTime, currentTime, page);
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
        }
        return null;

    }
}
