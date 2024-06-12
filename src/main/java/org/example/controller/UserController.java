package org.example.controller;

import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.service.UserLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user/")
public class UserController {

    private final UserLoginService userLoginService;

    public UserController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangeUserLoginPasswordDto changeUserLoginPasswordDto) {
        if (userLoginService.changePassword(changeUserLoginPasswordDto)) {
            return ResponseEntity.ok("Successfully changed password");
        }
        return ResponseEntity.badRequest().body("Somewhere appeared error while changing password");
    }

}
