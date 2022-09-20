package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.common.Mapper;
import ru.practicum.exceptions.NotExistedItemRequestException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.user.UserJpaRepository;
import ru.practicum.util.OffsetLimitPageable;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestJpaRepository itemRequestJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final Mapper mapper;
    private static final String header = "X-Sharer-User-Id";


    public ItemRequestDTO createItemRequest(long userId, @Valid ItemRequestDTO itemRequestDTO) {
        userJpaRepository.findById(userId).orElseThrow(NotExistedUserException::new);
        itemRequestDTO.setRequesterId(userId);
        itemRequestDTO.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = mapper.toItemRequest(itemRequestDTO);
        return mapper.toItemRequestDTO(itemRequestJpaRepository.save(itemRequest));
    }

    public List<ItemRequestDTO> getItemRequestsOfUser(long userId) {
        userJpaRepository.findById(userId).orElseThrow(NotExistedUserException::new);
        List<ItemRequest> itemRequestList = itemRequestJpaRepository.findAllByRequesterId(userId);
        return itemRequestList.stream().map(mapper::toItemRequestDTO).collect(Collectors.toList());
    }


    public List<ItemRequestDTO> getItemRequestsOfOther(long userId, Integer from, Integer size) {
        Pageable page = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        Page<ItemRequest> itemRequestPage = itemRequestJpaRepository.findAllByRequesterIdNot(userId, page);
        return itemRequestPage.getContent().stream().map(mapper::toItemRequestDTO).collect(Collectors.toList());
    }

    public ItemRequestDTO getItemRequestById(long userId, long itemRequestId) {
        userJpaRepository.findById(userId).orElseThrow(NotExistedUserException::new);
        return mapper.toItemRequestDTO(itemRequestJpaRepository.findById(itemRequestId).orElseThrow(NotExistedItemRequestException::new));
    }
}
