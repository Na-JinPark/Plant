package com.example.plant.dto;

import com.example.plant.type.UserType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserInfo {

  @Getter
  public static class Request {

    @NotEmpty
    private String loginId;
  }

  @Setter
  @Builder
  public static class Response {

    private String loginId;
    private String loginPassword;
    private String nickName;
    private UserType userType;

    public static Response from(UserDto userDto) {
      return Response.builder()
          .loginId(userDto.getLoginId())
          .loginPassword(userDto.getLoginPassword())
          .nickName(userDto.getNickName())
          .userType(userDto.getUserType())
          .build();
    }

  }

}
