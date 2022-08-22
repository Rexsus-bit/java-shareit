package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long booker_id, LocalDateTime currentTime);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long booker_id, LocalDateTime currentTime);
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long booker_id, LocalDateTime currentTime, LocalDateTime currentTime2);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime currentTime);
    List<Booking> findAllByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime currentTime);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime currentTime);


    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime current_time);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime current_time, LocalDateTime current_time1);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status waiting);
}
