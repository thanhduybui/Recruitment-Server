package com.edu.hcmute.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptUtils {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainTextPassword) {
        return bCryptPasswordEncoder.encode(plainTextPassword);
    }

    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return bCryptPasswordEncoder.matches(plainTextPassword, hashedPassword);
    }
}

