package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class LoginDto {

    @NotNull
    private String username;


    @NotNull
    private String password;

}
