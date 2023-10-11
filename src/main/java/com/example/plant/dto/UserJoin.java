package com.example.plant.dto;

import com.example.plant.type.UserType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserJoin {

  @Getter
  public static class Request {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String loginPassword;
    @NotEmpty
    private String nickName;
    @NotEmpty
    private UserType userType;
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
          .loginPassword(userDto.getNickName())
          .userType(userDto.getUserType())
          .build();
    }

  }

}
