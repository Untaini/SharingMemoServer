package me.untaini.sharingmemo.repository;

import me.untaini.sharingmemo.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindAndDelete() {
        User user = User.builder()
                //.id(1L)
                .sid("test_id")
                .password("test_password")
                .name("test_name")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getSid()).isEqualTo("test_id");
        assertThat(savedUser.getPassword()).isEqualTo("test_password");
        assertThat(savedUser.getName()).isEqualTo("test_name");
        assertThat(savedUser.getId()).isGreaterThan(0);

        User foundUser = userRepository.findBySid("test_id");

        assertThat(foundUser.getId()).isNotNull();
        assertThat(foundUser.getSid()).isEqualTo("test_id");
        assertThat(foundUser.getPassword()).isEqualTo("test_password");
        assertThat(foundUser.getName()).isEqualTo("test_name");
        assertThat(foundUser.getId()).isGreaterThan(0);

         userRepository.delete(foundUser);
    }
}
