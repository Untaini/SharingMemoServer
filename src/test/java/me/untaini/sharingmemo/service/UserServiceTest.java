package me.untaini.sharingmemo.service;

import me.untaini.sharingmemo.dto.UserRegisterRequestDTO;
import me.untaini.sharingmemo.dto.UserRegisterResponseDTO;
import me.untaini.sharingmemo.exception.UserException;
import me.untaini.sharingmemo.exception.type.UserExceptionType;
import me.untaini.sharingmemo.mapper.UserMapper;
import me.untaini.sharingmemo.repository.UserRepository;
import me.untaini.sharingmemo.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공")
    public void testRegisterSuccess() {
        //given
        UserRegisterRequestDTO userRequest = UserRegisterRequestDTO.builder()
                .id("test_id")
                .password("test_password")
                .confirmPassword("test_password")
                .name("test")
                .build();

        User user = UserMapper.INSTANCE.userRegisterRequestDTOToUser(userRequest);
        ReflectionTestUtils.setField(user, "id", 1L);

        //mocking
        given(userRepository.save(any()))
                .willReturn(user);
        given(userRepository.findBySid(userRequest.getId()))
                .willReturn(user);

        //when
        UserRegisterResponseDTO userResponse = userService.register(userRequest);

        //then
        User foundUser = userRepository.findBySid(userResponse.getId());

        assertThat(userResponse.getId()).isEqualTo("test_id");
        assertThat(userResponse.getName()).isEqualTo("test");

        assertThat(foundUser.getSid()).isEqualTo("test_id");
        assertThat(foundUser.getPassword()).isEqualTo("test_password");
        assertThat(foundUser.getName()).isEqualTo("test");
    }

    @Test
    @DisplayName("회원가입 실패 (중복 ID)")
    public void testRegisterThrowExistId() {
        //given
        UserRegisterRequestDTO userRequest = UserRegisterRequestDTO.builder()
                .id("test_id")
                .password("test_password2")
                .confirmPassword("test_password2")
                .name("test2")
                .build();

        //mocking
        given(userRepository.existsBySid(userRequest.getId()))
                .willReturn(true);

        //when & then
        assertThat(
                assertThrows(UserException.class, () -> userService.register(userRequest)).getExceptionType())
                .isEqualTo(UserExceptionType.ALREADY_EXIST_ID);
    }

    @Test
    @DisplayName("회원가입 실패 (비밀번호 불일치)")
    public void testRegisterThrowNotMatchPassword() {
        //given
        UserRegisterRequestDTO userRequest = UserRegisterRequestDTO.builder()
                .id("test_id")
                .password("test_password2")
                .confirmPassword("test_password")
                .name("test2")
                .build();

        //mocking
        given(userRepository.existsBySid(userRequest.getId()))
                .willReturn(false);

        //when & then
        assertThat(
                assertThrows(UserException.class, () -> userService.register(userRequest)).getExceptionType())
                .isEqualTo(UserExceptionType.NOT_MATCH_PASSWORD);
    }
}
