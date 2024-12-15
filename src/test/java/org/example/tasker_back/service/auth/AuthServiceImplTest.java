//package org.example.tasker_back.service.auth;
//
//import org.example.tasker_back.dto.user.AuthResponse;
//import org.example.tasker_back.dto.user.RegistrationRequest;
//import org.example.tasker_back.enums.Role;
//import org.example.tasker_back.model.User;
//import org.example.tasker_back.repository.UserRepository;
//import org.example.tasker_back.security.JwtService;
//import org.example.tasker_back.service.user.auth.AuthServiceImpl;
//import org.example.tasker_back.utils.UserMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class AuthServiceImplTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private JwtService jwtService;
//
//    @InjectMocks
//    private AuthServiceImpl authService;
//
//
//    @Test
//    void register_SuccessfulRegistration() {
//        // Arrange
//        RegistrationRequest request = new RegistrationRequest("Tom Holland", "password123", "test@gmail.com", List.of(Role.DESIGNER, Role.ACCOUNTANT));
//        User user = new User();
//        user.setEmail(request.getEmail());
//
//        when(UserMapper.createUser(any(), any())).thenReturn(user);
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(jwtService.generateToken(user.getEmail())).thenReturn("mockedToken");
//
//        // Act
//        AuthResponse response = authService.register(request);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("mockedToken", response.getToken());
//        assertEquals(request.getEmail(), response.getUserDto().getEmail());
//
//        verify(userRepository).save(any(User.class));
//        verify(jwtService).generateToken(user.getEmail());
//    }
//
//
//}
