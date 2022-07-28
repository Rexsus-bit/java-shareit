package com.example.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    UserDTO create(@RequestBody @NotBlank @Valid User user) {
        return userService.create(user);
    }

    @GetMapping("{userId}")
    UserDTO get(@PathVariable long userId) {
        return userService.get(userId);
    }

    @GetMapping
    List<UserDTO> getAll() {
        return userService.getAll();
    }

    @PatchMapping("{userId}")
    UserDTO update(@RequestBody User user, @PathVariable long userId) {
        user.setId(userId);
        return userService.update(user, userId);

    }

    @DeleteMapping("{userId}")
    UserDTO delete(@PathVariable long userId) {
        return userService.delete(userId);
    }

}
