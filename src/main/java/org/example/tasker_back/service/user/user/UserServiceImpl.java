package org.example.tasker_back.service.user.user;


import lombok.RequiredArgsConstructor;
import org.example.tasker_back.dto.user.AuthResponse;
import org.example.tasker_back.dto.user.UpdatePasswordRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;
import org.example.tasker_back.model.User;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.security.JwtService;
import org.example.tasker_back.utils.UserMapper;
import org.example.tasker_back.utils.UserValidation;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;


    @Override
    public AuthResponse updateUser(UpdateUserRequest request) {
        User userDb = userRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserValidation.isValidUpdatingUser(request, userDb.getEmail(), userRepository);

        userRepository.save(UserMapper.updateUser(request, userDb));

        return AuthResponse.builder()
                .token(jwtService.generateToken(userDb.getEmail()))
                .userDto(UserMapper.toDTO(userDb))
                .build();
    }

    @Override
    public AuthResponse updatePassword(UpdatePasswordRequest request) {
        User userDb = userRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserValidation.isValidUpdatingPassword(request, userDb.getPassword());

        userDb.setPassword(BCrypt.hashpw(request.getCurrentPassword(), BCrypt.gensalt()));
        userRepository.save(userDb);

        return AuthResponse.builder()
                .token(jwtService.generateToken(userDb.getEmail()))
                .userDto(UserMapper.toDTO(userDb))
                .build();
    }
}

// todo when i update user email also update all tasks where was this user