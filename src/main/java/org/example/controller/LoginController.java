package org.example.controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.LoginChangePasswordDto;
import org.example.entity.Login;
import org.example.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@NoArgsConstructor
public class LoginController {

    private LoginService loginService;

    @PutMapping
    public ResponseEntity<Boolean> changePassword(@RequestBody LoginChangePasswordDto loginChangePasswordDto) {
        if (loginService.changePassword(loginChangePasswordDto)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

}
