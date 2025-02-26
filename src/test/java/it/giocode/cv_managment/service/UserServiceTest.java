package it.giocode.cv_managment.service;

import it.giocode.cv_managment.dto.req.user.UserCreationReqDto;
import it.giocode.cv_managment.dto.req.user.UserLoginReqDto;
import it.giocode.cv_managment.entity.UserEntity;
import it.giocode.cv_managment.exception.exception_class.AlreadyExistsException;
import it.giocode.cv_managment.exception.exception_class.IncorrectPasswordsException;
import it.giocode.cv_managment.repository.UserRepository;
import it.giocode.cv_managment.service.impl.UserServiceImpl;
import it.giocode.cv_managment.util.JwtUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private static UserCreationReqDto userCreationReqDto;
    private static UserLoginReqDto userLoginReqDto;
    private static UserEntity user;

    @BeforeAll
    static void setUp() {
        userCreationReqDto = UserCreationReqDto.builder()
                .email("test@example.com")
                .password("testPassword")
                .build();

        userLoginReqDto = UserLoginReqDto.builder()
                .email("test@example.com")
                .password("testPassword")
                .build();

        user = UserEntity.builder()
                .email(userCreationReqDto.getEmail())
                .password(userCreationReqDto.getPassword())
                .build();
    }

    @Test
    void userRegistration_WhenUserDoesNotExists_ShouldReturnTrue() {
        when(userRepository.existsByEmail(userCreationReqDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        boolean result = userService.userRegistration(userCreationReqDto);

        assertTrue(result);
        verify(userRepository).existsByEmail(userCreationReqDto.getEmail());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void userRegistration_WhenUserDoesExists_ShouldThrowAlreadyExistsException() {
        when(userRepository.existsByEmail(userCreationReqDto.getEmail())).thenReturn(true);

        AlreadyExistsException exception =
                assertThrows(AlreadyExistsException.class, () -> userService.userRegistration(userCreationReqDto));

        assertEquals("Email 'test@example.com' already exists. Please insert a valid value",
                exception.getMessage());
    }

    @Test
    void userLogin_WhenUserWasFoundAndPasswordIsCorrect_ShouldReturnToken() {
        String encodedPassword = new BCryptPasswordEncoder().encode(userLoginReqDto.getPassword());

        UserEntity registeredUser = UserEntity.builder()
                .userId(1L)
                .email(userLoginReqDto.getEmail())
                .password(encodedPassword)
                .build();

        when(userRepository.findByEmail(userLoginReqDto.getEmail())).thenReturn(Optional.of(registeredUser));

        String expectedToken = "mockedJwtToken";
        when(jwtUtil.generateToken(registeredUser.getEmail(), registeredUser.getUserId(), registeredUser.getRole())).thenReturn(expectedToken);
        when(passwordEncoder.matches(userLoginReqDto.getPassword(), registeredUser.getPassword())).thenReturn(true);

        String result = userService.userLogin(userLoginReqDto);

        assertEquals(expectedToken, result);

        verify(userRepository).findByEmail(userLoginReqDto.getEmail());
        verify(jwtUtil).generateToken(registeredUser.getEmail(), registeredUser.getUserId(), registeredUser.getRole());
    }

    @Test
    void userLogin_WhenUserNotFound_ShouldReturnNull() {
        when(userRepository.findByEmail(userLoginReqDto.getEmail())).thenReturn(Optional.empty());

        String token = userService.userLogin(userLoginReqDto);

        assertNull(token);
        verify(userRepository).findByEmail(userLoginReqDto.getEmail());
    }

    @Test
    void userLogin_WhenUserWasFoundButPasswordIsIncorrect_ShouldThrowIncorrectPasswordException() {
        UserEntity invalidUser = UserEntity.builder()
                        .email("test@example.com")
                        .password("invalidPassword")
                        .build();

        when(userRepository.findByEmail(userLoginReqDto.getEmail())).thenReturn(Optional.of(invalidUser));

        IncorrectPasswordsException exception =
                assertThrows(IncorrectPasswordsException.class, () -> userService.userLogin(userLoginReqDto));

        assertEquals("Incorrect password. Please try again", exception.getMessage());
        verify(userRepository).findByEmail(userLoginReqDto.getEmail());
    }
}
