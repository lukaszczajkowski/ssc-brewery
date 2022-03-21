package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import guru.sfg.brewery.security.UrlParametersAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @Autowired
    //JpaUserDetailsService jpaUserDetailsService;

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public UrlParametersAuthFilter urlParametersAuthFilter(AuthenticationManager authenticationManager) {
        final UrlParametersAuthFilter filter = new UrlParametersAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
    // when we provide an instance of a password encoder, Spring Security is going to pick it up,
    // so we are overriding it

    @Bean
    PasswordEncoder passwordEncoder() {
        // default implementation for password encoders
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(urlParametersAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
        // adding the custom filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // do not use in production!
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/beers/find", "/beers*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();

        // h2 console config
        http.headers().frameOptions().sameOrigin();
    }
    /*
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("spring")
                .password("guru")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
     */


    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    // auth.userDetailsService(jpaUserDetailsService).passwordEncoder(passwordEncoder());

        /*
        auth.inMemoryAuthentication()
                .withUser("spring")
                // password encoder is required here
                .password("{bcrypt}$2a$13$O2PlsyuMQBlfKKvo2q8hGeKrlgnAGr1UKnO.catTrDLg.cs.xfNFe")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}73919b98ee03c39e972ef7202bf52ffa7d73397e68e91b158e1a7d4728c96d029f15400d3557010b")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("{ldap}{SSHA}4yDAXRjxi/mVIGUJHZ9OrwG8DMun/hQYXNs7Lw==")
                .roles("CUSTOMER");

         */
    //}
}
