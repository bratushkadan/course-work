package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.AuthToken;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.AuthRepository;
import com.example.repository.UserRepository;
import com.example.util.*;

import java.util.HashMap;
import java.util.UUID;

// @CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthRepository authRepository;
  @Autowired
  UserRepository userRepository;

  @PostMapping("/create_account")
  public Object createAccount(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("tel") String tel) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      String hashedPassword = Password.encrypt(password);
      // Создаем пользователя
      User createdUser = userRepository.save(new User(username, hashedPassword, tel));

      // Создаем authToken для пользователя
      authRepository.save(AuthToken.create(username, createdUser.getId()));

      return createdUser;
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new JsonError(String.format("Name '%s' is already taken.", username)));
  }

  @GetMapping("/token")
  public Object getToken(@RequestParam("username") String username, @RequestParam("password") String password) {
    User user = userRepository.findByUsername(username);

    if (user != null) {
      AuthToken token = authRepository.findByUserId(user.getId());

      if (token != null) {
        // Токен свежий
        if (!AuthToken.isStale(token)) {
          return token;
        }
        // Необходима перегенерация токена (на случай компрометации)
        authRepository.delete(token);
      }
      return authRepository.save(AuthToken.create(user.getUsername(), user.getId()));
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new JsonError("Invalid credentials."));
  }

  @PostMapping("/check_token_validity")
  public Object checkTokenValidity(@RequestParam("auth_token") String authToken) {
    AuthToken token = authRepository.findByAuthToken(authToken);

    HashMap<String, Object> response = new HashMap<>();
    response.put("ok", token != null && !AuthToken.isStale(token));

    return response;
  }
}
