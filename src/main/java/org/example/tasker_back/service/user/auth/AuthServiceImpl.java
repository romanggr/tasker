package org.example.tasker_back.service.user.auth;

import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.user.*;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.security.JwtService;
import org.example.tasker_back.utils.UserMapper;
import org.example.tasker_back.utils.UserValidation;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCrypt bcrypt;
    private final JwtService jwtService;

    @Transactional
    @Override
    public AuthResponse register(RegistrationRequest request) {
        UserValidation.isValidRegistration(request, userRepository);

        User user = UserMapper.createUser(request);
        User userDb = userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.generateToken(userDb.getEmail()))
                .userDto(UserMapper.toDTO(userDb))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User userDb = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Please provide valid email"));

        if (!BCrypt.checkpw(request.getPassword(), userDb.getPassword())) {
            throw new IllegalArgumentException("Please provide valid password");
        }

        return AuthResponse.builder()
                .token(jwtService.generateToken(userDb.getEmail()))
                .userDto(UserMapper.toDTO(userDb))
                .build();
    }




}

