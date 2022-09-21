package ru.practicum.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestJpaRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterId(long requesterId);

    Page<ItemRequest> findAllByRequesterIdNot(Long userId, Pageable pageable);

}
