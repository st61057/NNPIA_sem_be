package org.example.controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.ChangeUserLoginPasswordDto;
import org.example.dto.CreateProcedureDto;
import org.example.dto.CreateUserDto;
import org.example.entity.Procedure;
import org.example.entity.UserLogin;
import org.example.enums.ProcedureValidity;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangeUserLoginPasswordDto changeUserLoginPasswordDto) {
        if (userService.changePassword(changeUserLoginPasswordDto)) {
            return ResponseEntity.ok("Successfully changed password");
        }
        return ResponseEntity.badRequest().body("Somewhere appeared error while changing password");
    }

    @PostMapping
    public ResponseEntity<?> addNewUser(@RequestBody CreateUserDto createUserDto) {
        try {
            UserLogin createdUser = userService.addUser(createUserDto.getUsername(), createUserDto.getEmail(), createUserDto.getPassword());
            return ResponseEntity.ok(createdUser);
        } catch (Exception exception) {
            return ResponseEntity.status(400).body(exception.getMessage());
        }
    }

}
