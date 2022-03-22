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

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
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
    @Transactional
    public void run(String... args) throws Exception {
        loadUsers();
    }

    private void loadUsers() {
        if (userRepository.count() == 0) {
            // beer authorities
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

            // customer authorities
            Authority createCustomer = authorityRepository.save(Authority.builder()
                    .permission("customer.create")
                    .build());
            Authority updateCustomer = authorityRepository.save(Authority.builder()
                    .permission("customer.create")
                    .build());
            Authority readCustomer = authorityRepository.save(Authority.builder()
                    .permission("customer.read")
                    .build());
            Authority deleteCustomer = authorityRepository.save(Authority.builder()
                    .permission("customer.delete")
                    .build());

            // brewery authorities
            Authority createBrewery = authorityRepository.save(Authority.builder()
                    .permission("brewery.create")
                    .build());
            Authority updateBrewery = authorityRepository.save(Authority.builder()
                    .permission("brewery.create")
                    .build());
            Authority readBrewery = authorityRepository.save(Authority.builder()
                    .permission("brewery.read")
                    .build());
            Authority deleteBrewery = authorityRepository.save(Authority.builder()
                    .permission("brewery.delete")
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

            adminRole.setAuthorities(new HashSet<>(Set.of(createBeer, updateBeer, readBeer, deleteBeer, createCustomer, updateCustomer,
                    readCustomer, deleteCustomer, createBrewery, updateBrewery, readBrewery, deleteBrewery)));
            customerRole.setAuthorities(new HashSet<>(Set.of(readBeer, readCustomer, readBrewery)));
            userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

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

            userRepository.save(spring);
            userRepository.save(user);
            userRepository.save(customer);

            log.debug("Users Loaded: " + userRepository.count());
        }
    }
}
