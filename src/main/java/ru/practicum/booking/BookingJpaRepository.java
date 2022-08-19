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


    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime currentTime);
    List<Booking> findAllByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime currentTime);
}
