package org.example.dto;

import lombok.Data;

@Data
public class ChangeUserLoginPasswordDto {

    private String username;

    private String oldPassword;

    private String newPassword;

}
