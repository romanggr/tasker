package org.example.tasker_back.unit.utils;

import org.example.tasker_back.dto.user.RegistrationRequest;
import org.example.tasker_back.dto.user.UpdatePasswordRequest;
import org.example.tasker_back.dto.user.UpdateUserRequest;
import org.example.tasker_back.enums.Role;
import org.example.tasker_back.repository.UserRepository;
import org.example.tasker_back.utils.UserValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserValidationTest {
    @Mock
    UserRepository userRepository;

    @Test
    void isValidEmail_success() {
        String validEmail = "valid@email.com";

        UserValidation.isValidEmail(validEmail);
    }

    @Test
    void isValidEmail_empty() {
        String invalidEmail = "";

        assertThrows(IllegalArgumentException.class, () -> UserValidation.isValidEmail(invalidEmail));
    }

    @Test
    void isValidEmail_invalid() {
        String invalidEmail = "invalid-email";

        assertThrows(IllegalArgumentException.class, () -> UserValidation.isValidEmail(invalidEmail));
    }

    @Test
    void isEmailInUse_sameEmails_success() {
        String newEmail = "valid@email.com";
        String currentEmail = "valid@email.com";

        UserValidation.isEmailInUse(newEmail, currentEmail, userRepository);
    }

    @Test
    void isEmailInUse_success() {
        String newEmail = "valid@email.com";
        String currentEmail = "current@email.com";

        when(userRepository.existsByEmail(newEmail)).thenReturn(false);

        UserValidation.isEmailInUse(newEmail, currentEmail, userRepository);
        verify(userRepository).existsByEmail(newEmail);
    }

    @Test
    void isEmailInUse_inUse() {
        String newEmail = "valid@email.com";
        String currentEmail = "current@email.com";

        when(userRepository.existsByEmail(newEmail)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> UserValidation.isEmailInUse(newEmail, currentEmail, userRepository));
        verify(userRepository).existsByEmail(newEmail);
    }

    @Test
    void isValidPassword_success() {
        String validPassword = "ValidPassword123";

        UserValidation.isValidPassword(validPassword);
    }

    @Test
    void isValidPassword_empty() {
        String invalidPassword = "";

        assertThrows(IllegalArgumentException.class, () -> UserValidation.isValidPassword(invalidPassword));
    }

    @Test
    void isValidPassword_short() {
        String invalidPassword = "asq";

        assertThrows(IllegalArgumentException.class, () -> UserValidation.isValidPassword(invalidPassword));
    }

    @Test
    void isValidPassword_oneDigit() {
        String invalidPassword = "students";

        assertThrows(IllegalArgumentException.class, () -> UserValidation.isValidPassword(invalidPassword));
    }

    @Test
    void isValidPassword_oneLetter() {
        String invalidPassword = "12345678";

        assertThrows(IllegalArgumentException.class, () -> UserValidation.isValidPassword(invalidPassword));
    }

    @Test
    void isValidName_success() {
        String name = "Name";

        UserValidation.isValidName(name);
    }

    @Test
    void isValidName_empty() {
        String name = "";

        assertThrows(IllegalArgumentException.class, () -> UserValidation.isValidName(name));

    }

    @Test
    void isValidRegistration_success() {
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "password123",
                "test@gmail.com",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        UserValidation.isValidRegistration(request, userRepository);

        verify(userRepository).existsByEmail(request.getEmail());
    }

    @Test
    void isValidRegistration_emailAlreadyInUse() {
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "password123",
                "test@gmail.com",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                UserValidation.isValidRegistration(request, userRepository)
        );
        verify(userRepository).existsByEmail(request.getEmail());
    }

    @Test
    void isValidRegistration_invalidEmail() {
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "password123",
                "invalid-email",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );

        assertThrows(IllegalArgumentException.class, () ->
                UserValidation.isValidRegistration(request, userRepository)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void isValidRegistration_invalidPassword() {
        RegistrationRequest request = new RegistrationRequest(
                "Tom Holland",
                "pass",
                "test@gmail.com",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );
        assertThrows(IllegalArgumentException.class, () ->
                UserValidation.isValidRegistration(request, userRepository)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void isValidRegistration_inValidName() {
        RegistrationRequest request = new RegistrationRequest(
                "",
                "pass",
                "test@gmail.com",
                List.of(Role.DESIGNER, Role.ACCOUNTANT)
        );
        assertThrows(IllegalArgumentException.class, () ->
                UserValidation.isValidRegistration(request, userRepository)
        );

        verifyNoInteractions(userRepository);
    }


    @Test
    void isValidUpdatingUser_success() {
        UpdateUserRequest request = new UpdateUserRequest(
                "idlalalla",
                "test@gmail.com",
                "Tom Holland",
                null
        );

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        UserValidation.isValidUpdatingUser(request, "otherEmail@gmail.com", userRepository);
        verify(userRepository).existsByEmail(request.getEmail());
    }

    @Test
    void isValidUpdatingUser_emailAlreadyInUse() {
        UpdateUserRequest request = new UpdateUserRequest(
                "idlalalla",
                "test@gmail.com",
                "Tom Holland",
                null
        );

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () ->
                UserValidation.isValidUpdatingUser(request, "test1@gmail.com", userRepository)
        );
        verify(userRepository).existsByEmail(request.getEmail());
    }

    @Test
    void isValidUpdatingUser_inValidEmail() {
        UpdateUserRequest request = new UpdateUserRequest(
                "idlalalla",
                "invalid-email",
                "Tom Holland",
                null
        );

        assertThrows(IllegalArgumentException.class, () ->
                UserValidation.isValidUpdatingUser(request, "test@gmail.com", userRepository)
        );
        verifyNoInteractions(userRepository);
    }

    @Test
    void isValidUpdatingUser_inValidName() {
        UpdateUserRequest request = new UpdateUserRequest(
                "idlalalla",
                "test@gmail.com",
                "",
                null
        );

        assertThrows(IllegalArgumentException.class, () ->
                UserValidation.isValidUpdatingUser(request, "test1@gmail.com", userRepository)
        );
        verifyNoInteractions(userRepository);
    }

    @Test
    void isValidUpdatingPassword_successes() {
        UpdatePasswordRequest request = new UpdatePasswordRequest("id123", "newPassword123", "oldPassword123");
        String hashedPassword = "$2a$10$XwJmL0fDk0TtZgKpqyqLf.D5mX1m.tUNn5j5v6hXOU9zV6z9XjfwC";

        Mockito.mockStatic(BCrypt.class);
        when(BCrypt.checkpw(request.getCurrentPassword(), hashedPassword)).thenReturn(true);

        UserValidation.isValidUpdatingPassword(request, hashedPassword);
    }

    @Test
    void isValidUpdatingPassword_inValidNewPassword() {
        UpdatePasswordRequest request = new UpdatePasswordRequest("id123", "new", "oldPassword123");
        String hashedPassword = "$2a$10$XwJmL0fDk0TtZgKpqyqLf.D5mX1m.tUNn5j5v6hXOU9zV6z9XjfwC";

        assertThrows(IllegalArgumentException.class,
                () -> UserValidation.isValidUpdatingPassword(request, hashedPassword));
    }

    @Test
    void isValidUpdatingPassword_passwordNotSame() {
        UpdatePasswordRequest request = new UpdatePasswordRequest("id123", "newPassword123", "oldPassword123");
        String hashedPassword = "$2a$10$XwJmL0fDk0TtZgKpqyqLf.D5mX1m.tUNn5j5v6hXOU9zV6z9XjfwC";

        when(BCrypt.checkpw(request.getCurrentPassword(), hashedPassword)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> UserValidation.isValidUpdatingPassword(request, hashedPassword));
    }

}
