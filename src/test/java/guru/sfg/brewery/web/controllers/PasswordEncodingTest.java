package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncodingTest {

    final static String PASSWORD = "password";

    @Test
    void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();

        // ldap is using a random salt so the  results will be different
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));

        String encodedPassword = ldap.encode(PASSWORD);

        assertTrue(ldap.matches(PASSWORD, encodedPassword));
    }

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "ThisIsMySaltValue";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }

    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();

        System.out.println(noOp.encode(PASSWORD));
    }
}
