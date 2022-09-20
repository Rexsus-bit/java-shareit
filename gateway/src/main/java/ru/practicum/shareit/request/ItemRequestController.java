package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Mapper;
import ru.practicum.request.ItemRequestDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(header) long userId, @RequestBody @NonNull @Valid ItemRequestDTO itemRequestDTO) {
        return itemRequestClient.create(userId, itemRequestDTO);
    }

    @GetMapping
    public ResponseEntity<Object>  getItemRequestsOfUser(@RequestHeader(header) long userId) {
        return itemRequestClient.getItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object>  getItemRequestsOfOther(@RequestHeader(header) long userId, @Min(0) @RequestParam
            (required = false) Integer from, @Min(1) @RequestParam (required = false) Integer size) {
        return itemRequestClient.getItemRequestsOfOther(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    public ResponseEntity<Object>  getItemRequestById(@RequestHeader(header) long userId, @PathVariable long itemRequestId) {
        return itemRequestClient.getItemRequestById(userId, itemRequestId);

    }

}
