package ru.practicum.user;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users", schema = "public")
public class User {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(unique = true)
    private String email;

    public User() {
    }

}
