package org.example.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeUserLoginPasswordDto {
    @NotNull
    private String username;

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;

}
