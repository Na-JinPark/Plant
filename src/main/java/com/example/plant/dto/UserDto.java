package com.example.plant.dto;

import com.example.plant.domain.User;
import com.example.plant.type.UserStatus;
import com.example.plant.type.UserType;
import java.math.BigInteger;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

  private BigInteger userId;
  private String loginId;
  private String loginPassword;
  private UserType userType;
  private String nickName;
  private UserStatus userStatus;

  public static UserDto fromEntity(User user) {
    return UserDto.builder()
        .userId(user.getUserId())
        .loginId(user.getLoginId())
        .loginPassword(user.getLoginPassword())
        .userType(user.getUserType())
        .nickName(user.getNickName())
        .userStatus(user.getUserStatus())
        .build();
  }

}

