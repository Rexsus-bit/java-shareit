package ru.practicum.item;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_name", nullable = false)
    private String name;
    private String description;
    private Boolean available;
    @Column(name = "owner_id")
    private Long ownerId;
    @Column(name = "request")
    private Long requestId;
}
