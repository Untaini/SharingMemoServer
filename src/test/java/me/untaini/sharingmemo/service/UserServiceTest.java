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
    public void testRegisterSuccess() throws Exception {
        //given
        UserRegisterRequestDTO userRequest = UserRegisterRequestDTO.builder()
                .sid("test_id")
                .password("test_password")
                .name("test")
                .build();

        User user = UserMapper.INSTANCE.userRegisterRequestDTOToUser(userRequest);
        ReflectionTestUtils.setField(user, "id", 1L);

        //mocking
        given(userRepository.save(any()))
                .willReturn(user);
        given(userRepository.findBySid(userRequest.getSid()))
                .willReturn(user);

        //when
        UserRegisterResponseDTO userResponse = userService.register(userRequest);

        //then
        User foundUser = userRepository.findBySid(userResponse.getSid());

        assertThat(userRequest.getSid()).isEqualTo(foundUser.getSid());
        assertThat(userRequest.getPassword()).isEqualTo(foundUser.getPassword());
        assertThat(userRequest.getName()).isEqualTo(foundUser.getName());
    }

    @Test
    @DisplayName("회원가입 실패")
    public void testRegisterFailure() {
        //given
        UserRegisterRequestDTO userRequest = UserRegisterRequestDTO.builder()
                .sid("test_id")
                .password("test_password2")
                .name("test2")
                .build();

        //mocking
        given(userRepository.existsBySid(userRequest.getSid()))
                .willReturn(true);

        //when & then
        assertThat(
                assertThrows(UserException.class, () -> userService.register(userRequest)).getExceptionType())
                .isEqualTo(UserExceptionType.ALREADY_EXIST_ID);
    }

}
