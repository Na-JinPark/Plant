package com.example.plant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlantApplication {

  public static void main(String[] args) {
    SpringApplication.run(PlantApplication.class, args);
  }

}
