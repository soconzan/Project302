package org.example.project302.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Md5PasswordEncoderTest {

    @Test
    void encode() {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String rawPassword = "1234";
        String encodedPassword = encoder.encode(rawPassword);

        // Expected MD5 hash of "1234"
        String expectedEncodedPassword = "81dc9bdb52d04dc20036dbd8313ed055";

        assertEquals(expectedEncodedPassword, encodedPassword);
        System.out.println("Encoded Password: " + encodedPassword);
    }

    @Test
    void matches() {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String rawPassword = "1234";
        String encodedPassword = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encodedPassword));
        System.out.println("Password matches: " + encoder.matches(rawPassword, encodedPassword));
    }
}