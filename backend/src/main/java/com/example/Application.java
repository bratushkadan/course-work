package com.example;

import com.example.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableMongoRepositories
@EnableWebMvc
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}