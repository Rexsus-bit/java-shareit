package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.User;

import java.util.List;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {

//    @Query(value = "SELECT i FROM items i WHERE i.available = TRUE " +
//            "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(i.description) " +
//            "LIKE LOWER(CONCAT('%', :text, '%')))", nativeQuery = true)
//    List<Item> searchAvailableItems(String text);

    @Query(" select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))" +
             "and i.available = true")
    List<Item> searchAvailableItems(String text);

}

