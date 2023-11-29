package me.untaini.sharingmemo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.untaini.sharingmemo.dto.UserRegisterRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterResponseDTO;
import me.untaini.sharingmemo.entity.User;
import me.untaini.sharingmemo.exception.UserException;
import me.untaini.sharingmemo.exception.type.UserExceptionType;
import me.untaini.sharingmemo.mapper.UserMapper;
import me.untaini.sharingmemo.service.HttpSessionService;
import me.untaini.sharingmemo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private HttpSessionService sessionService;

    @Test
    @DisplayName("회원가입 성공")
    public void testPostRegisterSuccess() throws Exception {
        UserRegisterRequestDTO userRequest1 = UserRegisterRequestDTO.builder()
                .id("test_id")
                .password("test_password")
                .confirmPassword("test_password")
                .name("test")
                .build();

        String registerData = objectMapper.writeValueAsString(userRequest1);

        User user = UserMapper.INSTANCE.userRegisterRequestDTOToUser(userRequest1);
        UserRegisterResponseDTO userResponse1 = UserMapper.INSTANCE.userToUserRegisterResponseDTO(user);

        //given
        given(userService.register(any()))
                .willReturn(userResponse1);

        //when & then
        mvc.perform(post("/user/register")
                        .content(registerData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("test_id"))
                .andExpect(jsonPath("$.name").value("test"));

    }

    @Test
    @DisplayName("회원가입 실패 (중복 ID)")
    public void testPostRegisterCatchExistID() throws Exception {
        UserRegisterRequestDTO userRequest1 = UserRegisterRequestDTO.builder()
                .id("test_id")
                .password("test_password2")
                .confirmPassword("test_password2")
                .name("test2")
                .build();

        String registerData = objectMapper.writeValueAsString(userRequest1);

        //given
        given(userService.register(any()))
                .willThrow(new UserException(UserExceptionType.ALREADY_EXIST_ID));

        //when & then
        mvc.perform(post("/user/register")
                        .content(registerData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(UserExceptionType.ALREADY_EXIST_ID.getMessage()));

    }

    @Test
    @DisplayName("회원가입 실패 (비밀번호 불일치)")
    public void testPostRegisterCatchNotMatchPassword() throws Exception {
        UserRegisterRequestDTO userRequest1 = UserRegisterRequestDTO.builder()
                .id("test_id")
                .password("test_password")
                .confirmPassword("test_password2")
                .name("test2")
                .build();

        String registerData = objectMapper.writeValueAsString(userRequest1);

        //given
        given(userService.register(any()))
                .willThrow(new UserException(UserExceptionType.NOT_MATCH_PASSWORD));

        //when & then
        mvc.perform(post("/user/register")
                        .content(registerData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(UserExceptionType.NOT_MATCH_PASSWORD.getMessage()));

    }

}
