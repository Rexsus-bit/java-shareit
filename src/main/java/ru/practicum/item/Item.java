package ru.practicum.item;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
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
    private Long request;
    public Item() {
    }

    public Item(Long id, String name, String description, Boolean available, Long ownerId, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.request = request;
    }
}
