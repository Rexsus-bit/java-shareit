package com.example.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDTO {
    long id;
    String name;
    @NotBlank
    @Email
    String email;
}
