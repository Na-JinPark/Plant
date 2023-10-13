package com.example.plant.controller;


import com.example.plant.dto.UserInfo;
import com.example.plant.dto.UserJoin;
import com.example.plant.service.UserService;
import com.example.plant.type.UserStatus;
import jakarta.validation.Valid;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /*
   * 회원가입 api
   * 파라미터 : 사용자 아이디, 비밀번호, 닉네임, 사용자 유형
   * 성공응답 : 사용자 아이디, 비밀번호, 닉네임, 사용자 유형
   */
  @PostMapping("/user/join")
  public UserJoin.Response userJoin(
      @RequestBody @Valid UserJoin.Request request
  ) {
    return UserJoin.Response.from(
        userService.userJoin(
            request.getLoginId(),
            request.getLoginPassword(),
            request.getNickName(),
            request.getUserType()
        )
    );
  }

  /*
   * 닉네임변경 api
   * 파라미터 : 사용자 아이디, 변경할 닉네임
   * 성공응답 : 사용자 아이디, 비밀번호, 닉네임, 사용자 유형
   */
  @PostMapping("/user/{userId}/newNickName")
  public UserInfo.Response changeNickname(
      @PathVariable BigInteger userId,
      @RequestParam String newNickName
  ) {
    return UserInfo.Response.from(
        userService.changeNickName(userId, newNickName)
    );
  }

  @PostMapping("/user/{userId}/userStatus")
  public UserInfo.Response changeUserStatus(
      @PathVariable BigInteger userId,
      @RequestParam UserStatus userStatus
  ) {
    return UserInfo.Response.from(
        userService.changeUserStatus(userId, userStatus)
    );
  }

  /*
   * 회원가입 api
   * 파라미터 : 사용자 아이디
   * 성공응답 : 사용자 아이디, 비밀번호, 닉네임, 사용자 유형
   */
  @GetMapping("user/{userId}")
  public UserInfo.Response userInfo(
      @PathVariable BigInteger userId) {
    return UserInfo.Response.from(
        userService.userInfo(userId)
    );
  }
}
