package org.example.tasker_back.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.user.*;
import org.example.tasker_back.service.user.auth.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> registration(@RequestBody RegistrationRequest request){
        AuthResponse authResponse = authService.register(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(authResponse);
    }
}
