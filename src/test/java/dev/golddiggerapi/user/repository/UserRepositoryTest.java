package dev.golddiggerapi.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import dev.golddiggerapi.config.QuerydslConfig;
import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import dev.golddiggerapi.user.domain.User;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(QuerydslConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void existsByUsername() {
        Function<String, String> testEncodeFunction = (a) -> a;
        UserSignupRequest request = new UserSignupRequest("tester", "1234", false, "www");
        User user = new User(request, testEncodeFunction);
        userRepository.save(user);

        assertThat(userRepository.existsByUsername("tester")).isTrue();
    }

    @Test
    void findUserByUsername() {
        Function<String, String> testEncodeFunction = (a) -> a;
        UserSignupRequest request = new UserSignupRequest("tester", "1234", false, "www");
        User user = new User(request, testEncodeFunction);
        userRepository.save(user);

        assertThat(userRepository.findUserByUsername("tester").get()).isEqualTo(user);
    }

    @Test
    void findAllBySubscribeNotificationAndDiscordUrlNot() {
        Function<String, String> testEncodeFunction = (a) -> a;
        UserSignupRequest request1 = new UserSignupRequest("tester", "1234", false, "NONE");
        User user1 = new User(request1, testEncodeFunction);
        userRepository.save(user1);

        UserSignupRequest request2 = new UserSignupRequest("tester", "1234", true, "www");
        User user2 = new User(request2, testEncodeFunction);
        userRepository.save(user2);

        UserSignupRequest request3 = new UserSignupRequest("tester", "1234", true, "NONE");
        User user3 = new User(request3, testEncodeFunction);
        userRepository.save(user3);

        List<User> subscribeUsers = userRepository.findAllBySubscribeNotificationAndDiscordUrlNot(true, "NONE");

        assertThat(subscribeUsers.size()).isEqualTo(1);
    }
}