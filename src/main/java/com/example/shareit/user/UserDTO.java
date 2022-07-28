package com.example.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    @NotBlank
    @Email
    private String email;
}
