package ru.practicum.booking;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item bookedItem;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking() {
    }
}
