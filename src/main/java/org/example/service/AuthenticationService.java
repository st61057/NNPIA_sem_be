package org.example.service;

import org.example.config.JwtService;
import org.example.dto.AuthenticationResponse;
import org.example.dto.LoginDto;
import org.example.dto.UserLoginDto;
import org.example.entity.UserLogin;
import org.example.repository.UserLoginRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserLoginService userLoginService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(JwtService jwtService, PasswordEncoder passwordEncoder, UserLoginService userLoginService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userLoginService = userLoginService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse addUser(UserLoginDto request) throws Exception {
        try {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            UserLogin userLogin = userLoginService.addUser(request);
            String token = jwtService.createToken(userLogin);

            return new AuthenticationResponse(token);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public AuthenticationResponse authenticate(LoginDto request) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserLogin userLogin = userLoginService.findLoginByUsername(request.getUsername());
            if (userLogin == null) {
                throw new Exception("Non existing use authentication");
            }
            String token = jwtService.createToken(userLogin);

            return new AuthenticationResponse(token);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }

    }
}
