package com.example.util;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class Password {
  private static Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
      "78F37641-7DB0-4604-A16F-985A032CE56E", 128, 128, 64);

  public static String encrypt(String password) {
    return pbkdf2PasswordEncoder.encode(password);
  }

  public static boolean matches(String password, String encryptedPart) {
    return pbkdf2PasswordEncoder.matches(password, encryptedPart);
  }
}
