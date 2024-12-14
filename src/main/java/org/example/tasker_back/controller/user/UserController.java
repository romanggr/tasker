package org.example.tasker_back.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.user.*;
import org.example.tasker_back.service.user.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthServiceImpl authService;


    @PutMapping("/updateUser")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody UpdateUserRequest request){
        AuthResponse authResponse = authService.updateUser(request);
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<AuthResponse> updatePassword(@RequestBody UpdatePasswordRequest request){
        AuthResponse authResponse = authService.updatePassword(request);
        return ResponseEntity.ok(authResponse);
    }
}
