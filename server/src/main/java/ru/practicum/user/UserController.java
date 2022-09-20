package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Mapper;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO create(@RequestBody @NotBlank @Valid UserDTO userDTO) {
        return userService.create(Mapper.toUser(userDTO));
    }

    @GetMapping("{userId}")
    public UserDTO get(@PathVariable long userId) {
            return Mapper.toUserDto(userService.get(userId));
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAll().stream()
                .map(Mapper::toUserDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("{userId}")
    public UserDTO update(@RequestBody UserDTO userDTO, @PathVariable long userId) {
        userDTO.setId(userId);
        return userService.update(Mapper.toUser(userDTO));
    }

    @DeleteMapping("{userId}")
    public void delete(@PathVariable long userId) {
        userService.delete(userId);
    }

}
