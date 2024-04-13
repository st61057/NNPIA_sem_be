package org.example.service;

import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.entity.UserLogin;
import org.example.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private LoginRepository loginRepository;

    public UserLogin findLoginByUserName(String username) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Empty user name input");
        }
        UserLogin userLogin = loginRepository.findByUsername(username);
        if (userLogin == null) {
            throw new NoSuchElementException("User doesn't exist");
        }
        return userLogin;
    }

    public boolean changePassword(ChangeUserLoginPasswordDto changeUserLoginPasswordDto) {
        UserLogin userLogin = loginRepository.findByUsername(changeUserLoginPasswordDto.getUsername());

        if (userLogin == null) {
            throw new RuntimeException("Login doesn't exist!");
        }

        if (userLogin.getPassword() != changeUserLoginPasswordDto.getOldPassword()) {
            throw new RuntimeException("Old password doesn't match");
        }

        userLogin.setPassword(changeUserLoginPasswordDto.getNewPassword());
        return true;

    }


}
