package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadUsers();
    }

    private void loadUsers() {
        if (userRepository.count() == 0) {
            // bear authorities
            Authority createBeer = authorityRepository.save(Authority.builder()
                    .permission("beer.create")
                    .build());

            Authority updateBeer = authorityRepository.save(Authority.builder()
                    .permission("update.create")
                    .build());

            Authority readBeer = authorityRepository.save(Authority.builder()
                    .permission("beer.read")
                    .build());

            Authority deleteBeer = authorityRepository.save(Authority.builder()
                    .permission("beer.delete")
                    .build());

            Role adminRole = roleRepository.save(Role.builder()
                    .name("ADMIN")
                    .build());

            Role customerRole = roleRepository.save(Role.builder()
                    .name("CUSTOMER")
                    .build());

            Role userRole = roleRepository.save(Role.builder()
                    .name("USER")
                    .build());

            adminRole.setAuthorities(Set.of(createBeer, updateBeer, readBeer, deleteBeer));
            customerRole.setAuthorities(Set.of(readBeer));
            userRole.setAuthorities(Set.of(readBeer));

            roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

            User spring = User.builder()
                    .username("spring")
                    .password(passwordEncoder.encode("guru"))
                    .role(adminRole)
                    .build();

            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("password"))
                    .role(userRole)
                    .build();

            User customer = User.builder()
                    .username("scott")
                    .password(passwordEncoder.encode("tiger"))
                    .role(customerRole)
                    .build();

            userRepository.saveAll(List.of(spring, user, customer));

            log.debug("Users Loaded: " + userRepository.count());
        }
    }
}
