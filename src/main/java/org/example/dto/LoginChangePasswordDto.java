package org.example.dto;

import lombok.Data;

@Data
public class LoginChangePasswordDto {

    private String username;

    private String oldPassword;

    private String newPassword;

}
