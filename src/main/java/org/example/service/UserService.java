package org.example.service;

import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.entity.UserLogin;
import org.example.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private LoginRepository loginRepository;

    public UserLogin findLoginByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Empty user name input");
        }
        UserLogin userLogin = loginRepository.findByUsername(username);
        if (userLogin == null) {
            throw new NoSuchElementException("User doesn't exist");
        }
        return userLogin;
    }

    public void addUser(String username, String email, String password) {
        UserLogin existingUserLogin = findLoginByUsername(username);

        if (existingUserLogin == null) {
            UserLogin userLogin = new UserLogin();
            userLogin.setUsername(username);
            userLogin.setEmail(email);
            userLogin.setPassword(password);
        }

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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin login = loginRepository.findByUsername(username);
        if (login == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(login.getUsername(), login.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }


}
