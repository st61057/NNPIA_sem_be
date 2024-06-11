package org.example.controller;

import org.example.config.JwtService;
import org.example.dto.AuthenticationResponse;
import org.example.entity.AuthToken;
import org.example.entity.UserLogin;
import org.example.service.AuthenticationService;
import org.example.service.UserLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    private final UserLoginService userLoginService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService, UserLoginService userLoginService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userLoginService = userLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserLogin userLogin) {
        try {
            AuthenticationResponse authenticationResponse = authenticationService.register(userLogin);
            return ResponseEntity.ok(authenticationResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(400).body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin) {
        authenticationService.authenticate(userLogin);
        UserLogin login = userLoginService.findLoginByUsername(userLogin.getUsername());
        String token = jwtService.createToken(login);
        return ResponseEntity.ok(new AuthToken(token, login.getUsername(), login.getId()));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout() throws AuthenticationException {
        return ResponseEntity.ok("Successfully log out");
    }
}
