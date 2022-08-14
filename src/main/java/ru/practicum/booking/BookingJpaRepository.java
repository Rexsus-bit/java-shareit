package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.item.Item;

@Repository
public interface BookingJpaRepository extends JpaRepository<Booking, Long> {
}
