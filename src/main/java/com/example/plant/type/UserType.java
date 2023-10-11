package com.example.plant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
  SELLER("판매자"),
  GENERAL("일반 사용자");

  private final String description;
}
