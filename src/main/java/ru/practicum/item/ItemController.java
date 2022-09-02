package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Mapper;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final Mapper mapper;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ItemDTO create(@RequestHeader(header) long userId, @RequestBody @NonNull @Valid ItemDTO itemDTO) {
        return mapper.toItemDTO(itemService.create(Mapper.toItem(itemDTO), userId));
    }

    @PatchMapping("{itemId}")
    public ItemDTO update(@RequestHeader(header) long userId, @RequestBody @NonNull ItemDTO itemDTO,
                          @PathVariable long itemId) {
        return mapper.toItemDTO(itemService.update(Mapper.toItem(itemDTO), userId, itemId));
    }

    @GetMapping("/{id}")
    public ItemDTO getItem(@RequestHeader(header) long userId, @PathVariable("id") long itemId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDTO> getAllUserItem(@RequestHeader(header) long userId, @Min(0) @RequestParam (required = false)
    Integer from, @Min(1) @RequestParam (required = false) Integer size) {
        return itemService.getAllUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDTO> search(@RequestParam String text) {
        return itemService.searchAvailableItems(text)
                .stream()
                .map(mapper::toItemDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("{itemId}/comment")
    public CommentDTO addComment(@RequestHeader(header) long userId,
                                 @RequestBody @NonNull @Valid Comment comment, @PathVariable long itemId) {
        return itemService.addComment(userId, comment, itemId);
    }


}
