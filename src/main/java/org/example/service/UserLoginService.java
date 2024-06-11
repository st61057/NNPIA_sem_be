package org.example.service;

import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.entity.UserLogin;
import org.example.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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

    public UserLogin addUser(String username, String email, String password) throws Exception {
        UserLogin existingUserLogin = findLoginByUsername(username);
        if (existingUserLogin == null) {
            UserLogin userLogin = new UserLogin();
            userLogin.setUsername(username);
            userLogin.setEmail(email);
            userLogin.setPassword(password);
            return userLoginRepository.save(userLogin);
        }
        throw new Exception("User with this username already exists");
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
