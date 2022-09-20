package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Mapper;
import ru.practicum.user.UserDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @NotBlank @Valid UserDTO userDTO) {
        return userClient.create(userDTO);
    }


    @GetMapping("{userId}")
    public ResponseEntity<Object> get(@PathVariable long userId) {
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> update(@RequestBody UserDTO userDTO, @PathVariable long userId) {
        return userClient.update(userDTO, userId);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        return userClient.deleteById(userId);
    }

}


