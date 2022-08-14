package ru.practicum.user;

import ru.practicum.common.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exceptions.NotExistedUserException;

import javax.persistence.EntityNotFoundException;
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
        return Mapper.toUserDto(userService.create(Mapper.toUser(userDTO)));
    }

    @GetMapping("{userId}")
    public UserDTO get(@PathVariable long userId) {
        try {
            return Mapper.toUserDto(userService.get(userId));
        } catch (
                EntityNotFoundException e) {
            throw new NotExistedUserException();
        }
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAll().stream()
                .map(Mapper::toUserDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("{userId}")
    public UserDTO update(@RequestBody UserDTO userDTO, @PathVariable long userId) throws IllegalAccessException {
        userDTO.setId(userId);
        return Mapper.toUserDto(userService.update(Mapper.toUser(userDTO)));
    }

    @DeleteMapping("{userId}")
    public void delete(@PathVariable long userId) {
        userService.delete(userId);
    }

}