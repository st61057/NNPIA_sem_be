package org.example.service;

import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.dto.UserLoginDto;
import org.example.entity.UserLogin;
import org.example.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserLoginService implements UserDetailsService {

    @Autowired
    private UserLoginRepository userLoginRepository;

    public UserLogin findLoginByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Empty user name input");
        }
        UserLogin userLogin = userLoginRepository.findByUsername(username);
        if (userLogin == null) {
            throw new NoSuchElementException("User doesn't exist");
        }
        return userLogin;
    }

    public boolean doesUserExist(String username) {
        return userLoginRepository.findByUsername(username) == null;
    }

    public UserLogin addUser(UserLoginDto userLoginDto) throws Exception {
        try {
            boolean existingUserLogin = doesUserExist(userLoginDto.getUsername());
            if (existingUserLogin) {
                UserLogin userLogin = new UserLogin();
                userLogin.setUsername(userLoginDto.getUsername());
                userLogin.setEmail(userLoginDto.getEmail());
                userLogin.setPassword(userLoginDto.getPassword());
                return userLoginRepository.save(userLogin);
            }else{
                throw new Exception("User with this name already exists");
            }
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public boolean changePassword(ChangeUserLoginPasswordDto changeUserLoginPasswordDto) {
        UserLogin userLogin = userLoginRepository.findByUsername(changeUserLoginPasswordDto.getUsername());

        if (userLogin == null) {
            throw new RuntimeException("Login doesn't exist!");
        }

        if (userLogin.getPassword() != changeUserLoginPasswordDto.getOldPassword()) {
            throw new RuntimeException("Old password doesn't match");
        }

        userLogin.setPassword(changeUserLoginPasswordDto.getNewPassword());
        return true;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin login = userLoginRepository.findByUsername(username);
        if (login == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return login;
    }

}
