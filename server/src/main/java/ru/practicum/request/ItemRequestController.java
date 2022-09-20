package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Mapper;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDTO createRequest(@RequestHeader(header) long userId, @RequestBody @NonNull @Valid ItemRequestDTO itemRequestDTO) {
        return itemRequestService.createItemRequest(userId, itemRequestDTO);
    }

    @GetMapping
    public List<ItemRequestDTO> getItemRequestsOfUser(@RequestHeader(header) long userId) {
        return itemRequestService.getItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDTO> getItemRequestsOfOther(@RequestHeader(header) long userId, @Min(0) @RequestParam (required = false) Integer from,
                                                        @Min(1) @RequestParam (required = false) Integer size) {
        return itemRequestService.getItemRequestsOfOther(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDTO getItemRequestById(@RequestHeader(header) long userId, @PathVariable long itemRequestId) {
        return itemRequestService.getItemRequestById(userId, itemRequestId);

    }

}
