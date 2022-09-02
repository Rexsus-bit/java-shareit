package ru.practicum.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable page);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime currentTime, LocalDateTime currentTime2, Pageable page);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status, Pageable page);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId, Pageable page);

    Page<Booking> findAllByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime currentTime);

    List<Booking> findAllByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime currentTime);

    Page<Booking> findAllByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime currentTime, LocalDateTime currentTime1, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status waiting, Pageable page);

    List<Booking> findAllByBookerIdAndItemIdAndStatusOrderByStartDesc(Long booker, Long itemId, Status status);
}
