package org.example.dto;

import lombok.Data;

@Data
public class CreateUserDto {
    String username;
    String email;
    String password;
}
