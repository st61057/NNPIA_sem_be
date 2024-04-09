package org.example.controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@NoArgsConstructor
public class UserController {

    private UserService userService;

    @PutMapping
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangeUserLoginPasswordDto changeUserLoginPasswordDto) {
        if (userService.changePassword(changeUserLoginPasswordDto)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

}
