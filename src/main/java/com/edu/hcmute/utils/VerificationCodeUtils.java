package com.edu.hcmute.utils;

import java.util.Random;

public class VerificationCodeUtils {
    public static String generateSixDigitCode() {
        // Random object to generate random numbers
        Random random = new Random();

        // StringBuilder to store the generated code
        StringBuilder code = new StringBuilder();

        // Generate 6 random digits
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0-9)
            code.append(digit); // Append the digit to the code
        }

        return code.toString(); // Return the 6-digit code as a string
    }
}
