package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadUsers();
    }

    private void loadUsers() {
        if (userRepository.count() == 0) {
            Authority admin = Authority.builder()
                    .role("ADMIN")
                    .build();

            Authority userAuthority = Authority.builder()
                    .role("USER")
                    .build();

            Authority customer = Authority.builder()
                    .role("CUSTOMER")
                    .build();

            authorityRepository.saveAll(List.of(admin, userAuthority, customer));

            User spring = User.builder()
                    .username("spring")
                    .password(passwordEncoder.encode("guru"))
                    .authority(admin)
                    .build();

            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("password"))
                    .authority(userAuthority)
                    .build();

            User scott = User.builder()
                    .username("scott")
                    .password(passwordEncoder.encode("tiger"))
                    .authority(customer)
                    .build();

            userRepository.saveAll(List.of(spring, user, scott));

            log.debug("Users Loaded: " + userRepository.count());
        }
    }
}
