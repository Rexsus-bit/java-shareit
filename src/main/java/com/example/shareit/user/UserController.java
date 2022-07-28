package com.example.shareit.user;

import com.example.shareit.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    UserDTO create(@RequestBody @NotBlank @Valid UserDTO userDTO) {
        return Mapper.toUserDto(userService.create(Mapper.toUser(userDTO)));
    }

    @GetMapping("{userId}")
    UserDTO get(@PathVariable long userId) {
        return Mapper.toUserDto(userService.get(userId));
    }

    @GetMapping
    List<UserDTO> getAll() {
        return userService.getAll().stream().map(Mapper::toUserDto).collect(Collectors.toList());
    }

    @PatchMapping("{userId}")
    UserDTO update(@RequestBody UserDTO userDTO, @PathVariable long userId) {
        userDTO.setId(userId);
        return Mapper.toUserDto(userService.update(Mapper.toUser(userDTO), userId));
    }

    @DeleteMapping("{userId}")
    UserDTO delete(@PathVariable long userId) {
        return Mapper.toUserDto(userService.delete(userId));
    }

}
