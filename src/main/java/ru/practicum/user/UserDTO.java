package ru.practicum.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    @NotBlank
    @Email
    private String email;
}
