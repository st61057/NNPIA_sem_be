package org.example.controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.entity.UserLogin;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangeUserLoginPasswordDto changeUserLoginPasswordDto) {
        if (userService.changePassword(changeUserLoginPasswordDto)) {
            return ResponseEntity.ok("Successfully changed password");
        }
        return ResponseEntity.badRequest().body("Somewhere appeared error while changing password");
    }

}
