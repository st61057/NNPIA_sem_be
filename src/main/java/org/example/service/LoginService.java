package org.example.service;

import org.bouncycastle.i18n.MissingEntryException;
import org.example.dto.LoginChangePasswordDto;
import org.example.entity.Login;
import org.example.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public Login findLoginByUserName(String username) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Empty user name input");
        }
        Login login = loginRepository.findByUserName(username);
        if (login == null) {
            throw new NoSuchElementException("User doesn't exist");
        }
        return login;
    }

    public boolean changePassword(LoginChangePasswordDto loginChangePasswordDto) {
        Login login = loginRepository.findByUserName(loginChangePasswordDto.getUsername());

        if (login == null) {
            throw new RuntimeException("Login doesn't exist!");
        }

        if (login.getPassword() != loginChangePasswordDto.getOldPassword()) {
            throw new RuntimeException("Old password doesn't match");
        }

        login.setPassword(loginChangePasswordDto.getNewPassword());
        return true;

    }


}
