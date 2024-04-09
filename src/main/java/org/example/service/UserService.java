package org.example.service;

import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.entity.User;
import org.example.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private LoginRepository loginRepository;

    public User findLoginByUserName(String username) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Empty user name input");
        }
        User user = loginRepository.findByUserName(username);
        if (user == null) {
            throw new NoSuchElementException("User doesn't exist");
        }
        return user;
    }

    public boolean changePassword(ChangeUserLoginPasswordDto changeUserLoginPasswordDto) {
        User user = loginRepository.findByUserName(changeUserLoginPasswordDto.getUsername());

        if (user == null) {
            throw new RuntimeException("Login doesn't exist!");
        }

        if (user.getPassword() != changeUserLoginPasswordDto.getOldPassword()) {
            throw new RuntimeException("Old password doesn't match");
        }

        user.setPassword(changeUserLoginPasswordDto.getNewPassword());
        return true;

    }


}
