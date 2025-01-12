package org.example.tasker_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.user.*;
import org.example.tasker_back.service.user.user.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;


    @PutMapping("/updateUser")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody UpdateUserRequest request){
        AuthResponse authResponse = userService.updateUser(request);
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<AuthResponse> updatePassword(@RequestBody UpdatePasswordRequest request){
        AuthResponse authResponse = userService.updatePassword(request);
        return ResponseEntity.ok(authResponse);
    }
}
