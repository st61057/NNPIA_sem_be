package org.example.service;

import org.example.config.JwtService;
import org.example.dto.AuthenticationResponse;
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

    private final UserLoginRepository userLoginRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(JwtService jwtService, PasswordEncoder passwordEncoder, UserLoginRepository userLoginRepository, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userLoginRepository = userLoginRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(UserLogin request) {
        UserLogin userLogin = new UserLogin();
        userLogin.setUsername(request.getUsername());
        userLogin.setEmail(request.getEmail());
        userLogin.setPassword(passwordEncoder.encode(request.getPassword()));

        userLogin = userLoginRepository.save(userLogin);

        String token = jwtService.createToken(userLogin);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(UserLogin request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserLogin userLogin = userLoginRepository.findByUsername(request.getUsername());
        String token = jwtService.createToken(userLogin);

        return new AuthenticationResponse(token);
    }
}
