package com.example.plant.dto;

import com.example.plant.type.UserType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class UserJoin {

  @Getter
  public static class Request {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String loginPassword;
    @NotEmpty
    private String nickName;
    @NotNull
    private UserType userType;
  }

  @Getter
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
