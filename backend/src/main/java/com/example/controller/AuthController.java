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

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthRepository authRepository;
  @Autowired
  UserRepository userRepository;

  @PostMapping("/create_account")
  public Object createAccount(@RequestParam("username") String username, @RequestParam("password") String password) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      // Создаем пользователя
      User createdUser = userRepository.save(new User(username, Password.encrypt(password)));
      
      // Создаем authToken для пользователя
      // authToken.save()
      
      return createdUser;
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new JsonError(String.format("Name '%s' is already taken.", username)));
  }

  @PostMapping("/check_token_validity")
  public Object checkTokenValidity(@RequestParam("auth_token") String authToken) {
    HashMap<String, Object> response = new HashMap<String, Object>();

    if (authToken.equals("badanga")) {
      response.put("ok", false);
    } else {
      response.put("ok", true);
    }

    return response;
  }

  // @GetMapping("token")
  // public AuthToken getToken(@RequestParam("username") String username,
  // @RequestParam("password") String password) {
  // return
  // }
}
