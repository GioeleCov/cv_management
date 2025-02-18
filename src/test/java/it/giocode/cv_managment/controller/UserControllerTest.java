package it.giocode.cv_managment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.giocode.cv_managment.dto.req.user.UserCreationReqDto;
import it.giocode.cv_managment.dto.req.user.UserLoginReqDto;
import it.giocode.cv_managment.service.TestSecurityConfig;
import it.giocode.cv_managment.service.iface.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserController userController;

    private UserCreationReqDto userCreationReqDto;
    private UserLoginReqDto userLoginReqDto;

    @BeforeEach
    public void setUp() {
        userCreationReqDto = UserCreationReqDto.builder()
                .email("test@gmail.com")
                .password("Test1@word")
                .build();


        userLoginReqDto = UserLoginReqDto.builder()
                .email("test@gmail.com")
                .password("Test1@word")
                .build();
    }

    @Test
    public void userRegistration_WhenServiceProcessTheCreationRequest_ShouldReturnStatus201() throws Exception {
        when(userService.userRegistration(any(UserCreationReqDto.class))).thenReturn(true);

        String requestJson = objectMapper.writeValueAsString(userCreationReqDto);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("User created successfully"));
    }

    @Test
    public void userRegistration_WhenServiceProcessFailTheCreationRequest_ShouldReturnStatus500() throws Exception {
        when(userService.userRegistration(any(UserCreationReqDto.class))).thenReturn(false);

        String requestJson = objectMapper.writeValueAsString(userCreationReqDto);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Something went wrong. Please try later"));
    }

    @Test
    public void userLogin_WhenServiceProcessTheLoginRequest_ShouldReturnStatus200() throws Exception {
        String expectedToken = "mockedJwtToken";

        when(userService.userLogin(any(UserLoginReqDto.class))).thenReturn(expectedToken);

        String requestJson = objectMapper.writeValueAsString(userLoginReqDto);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User logged in successfully"));
    }

    @Test
    public void userLogin_WhenServiceProcessFailTheLoginRequest_ShouldReturnStatus404() throws Exception {
        UserLoginReqDto invalidUser = UserLoginReqDto.builder()
                .email("nonexistent@gmail.com")
                .password("Test1@word!")
                .build();

        when(userService.userLogin(invalidUser)).thenReturn(null);
        String requestJson = objectMapper.writeValueAsString(invalidUser);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value("User not found with email 'nonexistent@gmail.com'"));
    }
}
